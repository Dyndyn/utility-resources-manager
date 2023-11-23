package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.service.dto.RowDTO;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.IntVector;
import smile.math.MathEx;
import smile.regression.LinearModel;
import smile.regression.OLS;
import smile.regression.RandomForest;
import smile.validation.CrossValidation;
import smile.validation.RegressionValidations;

@Service
public class ElectricityPredictionModel {

    private final Logger log = LoggerFactory.getLogger(ElectricityPredictionModel.class);

    private final ConsumptionHistoryRepository consumptionHistoryRepository;
    private RandomForest randomForest;

    public ElectricityPredictionModel(ConsumptionHistoryRepository consumptionHistoryRepository) {
        this.consumptionHistoryRepository = consumptionHistoryRepository;
    }

    @PostConstruct
    public void trainAndUseModel() {
        List<RowDTO> dtos = consumptionHistoryRepository.findByUtilityId(1L);

        Map<Long, Double> tempCorrelation = new HashMap<>();
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

        Map<Long, Double> dailyConsumptionPerHousehold = dtos
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
        countPerHousehold.forEach((key, totalDays) -> dailyConsumptionPerHousehold.compute(key, (k, v) -> v / totalDays));

        double[] mdcs = dailyConsumptionPerHousehold.values().stream().mapToDouble(Double::doubleValue).toArray();
        double mean = MathEx.mean(mdcs);
        double sdt = MathEx.sd(mdcs);
        double meanLog = Math.log(mean);
        Map<Long, Double> dailyConsumptionDeviation = dailyConsumptionPerHousehold
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Entry::getKey, es -> (Math.log(es.getValue()) - meanLog) / sdt));

        Map<Long, Double> trend = new HashMap<>();
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
            value
                .entrySet()
                .forEach(days ->
                    yearlyConsumptionPerHousehold
                        .get(householdId)
                        .compute(days.getKey(), (year, yearlyConsumption) -> yearlyConsumption / days.getValue())
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

        dtos.forEach(dto -> {
            dto.calculate();
            dto.setMeanDailyConsumption(dailyConsumptionPerHousehold.get(dto.getHouseholdId()));
            dto.setDailyConsumptionDeviation(dailyConsumptionDeviation.get(dto.getHouseholdId()));
            dto.setTrend(trend.get(dto.getHouseholdId()));
            dto.setTempCorrelation(tempCorrelation.get(dto.getHouseholdId()));
        });

        DataFrame df = DataFrame
            .of(dtos, RowDTO.class)
            .select(
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
                "tempCorrelation"
            );
        this.randomForest = RandomForest.fit(Formula.lhs("consumption"), df);

        RegressionValidations<RandomForest> validations = CrossValidation.regression(10, Formula.lhs("consumption"), df, RandomForest::fit);
        log.info("Cross-validation result: {}", validations);
    }
}
