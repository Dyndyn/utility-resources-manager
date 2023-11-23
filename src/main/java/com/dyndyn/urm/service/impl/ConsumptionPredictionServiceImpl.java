package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.ConsumptionPrediction;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.repository.ConsumptionPredictionRepository;
import com.dyndyn.urm.repository.HouseholdUtilityRepository;
import com.dyndyn.urm.repository.UtilityRepository;
import com.dyndyn.urm.service.ConsumptionPredictionService;
import com.dyndyn.urm.service.prediction.PredictionContext;
import com.dyndyn.urm.service.prediction.PredictionModel;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsumptionPredictionServiceImpl implements ConsumptionPredictionService {

    private final ConsumptionPredictionRepository consumptionPredictionRepository;
    private final HouseholdUtilityRepository householdUtilityRepository;
    private final UtilityRepository utilityRepository;
    private final PredictionContext predictionContext;

    public ConsumptionPredictionServiceImpl(
        ConsumptionPredictionRepository consumptionPredictionRepository,
        HouseholdUtilityRepository householdUtilityRepository,
        UtilityRepository utilityRepository,
        PredictionContext predictionContext
    ) {
        this.consumptionPredictionRepository = consumptionPredictionRepository;
        this.householdUtilityRepository = householdUtilityRepository;
        this.utilityRepository = utilityRepository;
        this.predictionContext = predictionContext;
    }

    @Transactional
    @Override
    public void generatePredictions(Long householdUtilityId) {
        HouseholdUtility householdUtility = householdUtilityRepository
            .findById(householdUtilityId)
            .orElseThrow(() -> new NoSuchElementException("Household Utility [%d] is not found".formatted(householdUtilityId)));
        PredictionModel predictionModel = predictionContext.getPredictionModel(householdUtility);
        LocalDate predictionStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        Map<LocalDate, ConsumptionPrediction> existingPredictions = householdUtility
            .getConsumptionPredictions()
            .stream()
            .filter(cp -> !cp.getDate().isBefore(predictionStart))
            .collect(Collectors.toMap(ConsumptionPrediction::getDate, Function.identity()));
        List<ConsumptionPrediction> allPredictions = IntStream
            .range(0, 11)
            .mapToObj(predictionStart::plusMonths)
            .map(month -> {
                ConsumptionPrediction cp = existingPredictions.computeIfAbsent(
                    month,
                    m -> new ConsumptionPrediction().date(m).householdUtility(householdUtility)
                );
                cp.setConsumption(predictionModel.predict(householdUtility, month));
                return cp;
            })
            .toList();
        allPredictions.forEach(cp -> cp.setConsumption(householdUtility.getRate()));
        consumptionPredictionRepository.saveAll(allPredictions);
    }
}
