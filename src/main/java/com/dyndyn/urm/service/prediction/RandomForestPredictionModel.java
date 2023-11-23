package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.domain.Temperature;
import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.repository.ConsumptionPredictionRepository;
import com.dyndyn.urm.repository.TemperatureRepository;
import com.dyndyn.urm.service.dto.RowDTO;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.math.MathEx;
import smile.regression.LinearModel;
import smile.regression.OLS;
import smile.regression.RandomForest;

public abstract class RandomForestPredictionModel implements PredictionModel {

    private final Logger log = LoggerFactory.getLogger(RandomForestPredictionModel.class);
    private static final String[] FEATURES = new String[] {
        "consumption",
        "consumptionLag1",
        "consumptionLag2",
        "area",
        "residents",
        "monthSin",
        "monthCos",
        "yearSin",
        "yearCos",
        "meanDailyConsumption",
        "dailyConsumptionDeviation",
        "trend",
        "tempCorrelation",
    };
    private final Long utilityId;
    private final ConsumptionHistoryRepository consumptionHistoryRepository;
    private final ConsumptionPredictionRepository consumptionPredictionRepository;
    private final TemperatureRepository temperatureRepository;
    private RandomForest randomForest;
    private final Map<Long, Double> tempCorrelation = new HashMap<>();
    private final Map<Long, Double> dailyConsumptionPerHousehold = new HashMap<>();
    private final Map<Long, Double> dailyConsumptionDeviation = new HashMap<>();
    private final Map<Long, Double> trend = new HashMap<>();

    protected RandomForestPredictionModel(
        Long utilityId,
        ConsumptionHistoryRepository consumptionHistoryRepository,
        ConsumptionPredictionRepository consumptionPredictionRepository,
        TemperatureRepository temperatureRepository
    ) {
        this.utilityId = utilityId;
        this.consumptionHistoryRepository = consumptionHistoryRepository;
        this.consumptionPredictionRepository = consumptionPredictionRepository;
        this.temperatureRepository = temperatureRepository;
    }

    @Override
    public BigDecimal predict(HouseholdUtility householdUtility, LocalDate date) {
        RowDTO rowDTO = new RowDTO(
            householdUtility.getHousehold().getId(),
            BigDecimal.ZERO,
            householdUtility.getHousehold().getArea(),
            householdUtility.getHousehold().getResidents(),
            date,
            temperatureRepository
                .findByCityAndDate(householdUtility.getHousehold().getCity(), date)
                .map(Temperature::getTemperature)
                .orElseThrow(() ->
                    new NoSuchElementException(
                        "Temperature not found for city [%s] and date [%s]".formatted(
                                householdUtility.getHousehold().getCity().getName(),
                                date
                            )
                    )
                )
        );
        LocalDate previousMonth = date.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate previous2Month = date.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
        rowDTO.setConsumptionLag1(
            Optional
                .ofNullable(
                    consumptionHistoryRepository.findConsumptionByHouseholdUtilityIdAndDate(householdUtility.getId(), previousMonth)
                )
                .or(() ->
                    Optional.ofNullable(
                        consumptionPredictionRepository.findConsumptionByHouseholdUtilityIdAndDate(householdUtility.getId(), previousMonth)
                    )
                )
                .orElseThrow(() ->
                    new NoSuchElementException(
                        "Consumption not found for household utility [%d] and date [%s]".formatted(householdUtility.getId(), previousMonth)
                    )
                )
                .doubleValue()
        );
        rowDTO.setConsumptionLag2(
            Optional
                .ofNullable(
                    consumptionHistoryRepository.findConsumptionByHouseholdUtilityIdAndDate(householdUtility.getId(), previous2Month)
                )
                .or(() ->
                    Optional.ofNullable(
                        consumptionPredictionRepository.findConsumptionByHouseholdUtilityIdAndDate(householdUtility.getId(), previous2Month)
                    )
                )
                .orElseThrow(() ->
                    new NoSuchElementException(
                        "Consumption not found for household utility [%d] and date [%s]".formatted(householdUtility.getId(), previous2Month)
                    )
                )
                .doubleValue()
        );
        calculateAllFeatures(rowDTO);
        double result = randomForest.predict(DataFrame.of(List.of(rowDTO), RowDTO.class).select(FEATURES).get(0));
        return BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean support(HouseholdUtility householdUtility) {
        return (
            householdUtility.getUtilityProvider().getUtility().getPredictable() &&
            householdUtility.getUtilityProvider().getUtility().getId().equals(utilityId)
        );
    }

    protected DataFrame getDataSet() {
        List<RowDTO> dtos = consumptionHistoryRepository.findRowByUtilityId(utilityId);

        Map<Long, List<Double>> householdConsumption = dtos
            .stream()
            .collect(Collectors.groupingBy(RowDTO::getHouseholdId, Collectors.mapping(RowDTO::getConsumption, Collectors.toList())));
        Map<Long, List<Double>> householdTemperature = dtos
            .stream()
            .collect(Collectors.groupingBy(RowDTO::getHouseholdId, Collectors.mapping(RowDTO::getTemperature, Collectors.toList())));
        householdTemperature.forEach((householdId, temperatures) -> {
            double cor = MathEx.cor(
                householdConsumption.get(householdId).stream().mapToDouble(d -> d).toArray(),
                temperatures.stream().mapToDouble(d -> d).toArray()
            );
            tempCorrelation.put(householdId, cor);
        });

        Map<Long, Double> allConsumptionPerHousehold = dtos
            .stream()
            .collect(
                Collectors.groupingBy(RowDTO::getHouseholdId, Collectors.mapping(RowDTO::getConsumption, Collectors.summingDouble(d -> d)))
            );
        Map<Long, Integer> countPerHousehold = dtos
            .stream()
            .collect(
                Collectors.groupingBy(
                    RowDTO::getHouseholdId,
                    Collectors.mapping(
                        RowDTO::getMonth,
                        Collectors.mapping(LocalDate::lengthOfMonth, Collectors.summingInt(lengthOfMonth -> lengthOfMonth))
                    )
                )
            );
        countPerHousehold.forEach((key, totalDays) -> dailyConsumptionPerHousehold.put(key, allConsumptionPerHousehold.get(key) / totalDays)
        );

        double[] mdcs = dailyConsumptionPerHousehold.values().stream().mapToDouble(Double::doubleValue).toArray();
        double mean = MathEx.mean(mdcs);
        double sdt = MathEx.sd(mdcs);
        double meanLog = Math.log(mean);
        dailyConsumptionPerHousehold.forEach((householdId, dailyConsumption) ->
            dailyConsumptionDeviation.put(householdId, (Math.log(dailyConsumption) - meanLog) / sdt)
        );

        Map<Long, Map<Integer, Double>> yearlyConsumptionPerHousehold = dtos
            .stream()
            .collect(
                Collectors.groupingBy(
                    RowDTO::getHouseholdId,
                    Collectors.groupingBy(dto -> dto.getMonth().getYear(), Collectors.summingDouble(RowDTO::getConsumption))
                )
            );
        Map<Long, Map<Integer, Integer>> yearlyDaysCountPerHousehold = dtos
            .stream()
            .collect(
                Collectors.groupingBy(
                    RowDTO::getHouseholdId,
                    Collectors.groupingBy(dto -> dto.getMonth().getYear(), Collectors.summingInt(dto -> dto.getMonth().lengthOfMonth()))
                )
            );
        yearlyDaysCountPerHousehold.forEach((householdId, value) ->
            value.forEach((key, value1) ->
                yearlyConsumptionPerHousehold.get(householdId).compute(key, (year, yearlyConsumption) -> yearlyConsumption / value1)
            )
        );

        yearlyConsumptionPerHousehold.forEach((householdId, yearlyData) -> {
            if (yearlyData.size() <= 2) {
                trend.put(householdId, 0D);
                return;
            }
            double[][] array = yearlyData
                .entrySet()
                .stream()
                .sorted(Entry.comparingByKey())
                .map(e -> new double[] { e.getKey(), e.getValue() })
                .toArray(double[][]::new);
            DataFrame df = DataFrame.of(array, "Year", "MeanDailyConsumption");
            LinearModel linearModel = OLS.fit(Formula.lhs("MeanDailyConsumption"), df);
            double coefficient = linearModel.coefficients()[0];
            trend.put(householdId, coefficient);
        });

        Map<Long, List<RowDTO>> orderedValues = dtos
            .stream()
            .sorted(Comparator.comparing(RowDTO::getMonth))
            .collect(Collectors.groupingBy(RowDTO::getHouseholdId));
        orderedValues.forEach((householdId, rows) -> {
            for (int i = rows.size() - 1; i >= 2; i--) {
                rows.get(i).setConsumptionLag1(rows.get(i - 1).getConsumption());
                rows.get(i).setConsumptionLag2(rows.get(i - 2).getConsumption());
            }
            rows.remove(0);
            rows.remove(0);
        });
        dtos = orderedValues.values().stream().flatMap(Collection::stream).toList();

        dtos.forEach(this::calculateAllFeatures);

        return DataFrame.of(dtos, RowDTO.class).select(FEATURES);
    }

    private void calculateAllFeatures(RowDTO dto) {
        dto.calculate();
        dto.setMeanDailyConsumption(dailyConsumptionPerHousehold.get(dto.getHouseholdId()));
        dto.setDailyConsumptionDeviation(dailyConsumptionDeviation.get(dto.getHouseholdId()));
        dto.setTrend(trend.get(dto.getHouseholdId()));
        dto.setTempCorrelation(tempCorrelation.get(dto.getHouseholdId()));
    }

    @PostConstruct
    protected void initializeModel() {
        this.randomForest = RandomForest.fit(Formula.lhs("consumption"), getDataSet());
    }
    //    @PostConstruct
    //    protected void loadModel() {
    //        ClassPathResource serializedModel = new ClassPathResource("models/" + utilityId + ".ser");
    //        if (serializedModel.exists()) {
    //            try (InputStream is = serializedModel.getInputStream();
    //                ObjectInputStream ois = new ObjectInputStream(is);) {
    //                this.randomForest = (RandomForest) ois.readObject();
    //            } catch (ClassNotFoundException | IOException e) {
    //                log.error("Cannot load model from file for utility [%s]".formatted(utilityId), e);
    //            }
    //        } else {
    //            trainModel();
    //            try (ObjectOutputStream oos = new ObjectOutputStream(
    //                new FileOutputStream(serializedModel.getFile()))) {
    //                oos.writeObject(randomForest);
    //                log.info("RandomForest model saved successfully for utility [{}]", utilityId);
    //            } catch (IOException e) {
    //                log.error("Cannot save model to file for utility [%s]".formatted(utilityId), e);
    //            }
    //        }
    //    }

}
