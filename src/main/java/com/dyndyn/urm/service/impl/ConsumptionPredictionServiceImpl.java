package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.ConsumptionPrediction;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.repository.ConsumptionPredictionRepository;
import com.dyndyn.urm.repository.UtilityRepository;
import com.dyndyn.urm.service.ConsumptionPredictionService;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsumptionPredictionServiceImpl implements ConsumptionPredictionService {

    private final ConsumptionPredictionRepository consumptionPredictionRepository;
    private final UtilityRepository utilityRepository;

    public ConsumptionPredictionServiceImpl(
        ConsumptionPredictionRepository consumptionPredictionRepository,
        UtilityRepository utilityRepository
    ) {
        this.consumptionPredictionRepository = consumptionPredictionRepository;
        this.utilityRepository = utilityRepository;
    }

    @Transactional
    @Override
    public void generatePredictions(HouseholdUtility householdUtility) {
        Utility utility = utilityRepository.findByUtilityProviders(householdUtility.getUtilityProvider());
        if (Boolean.TRUE.equals(utility.getConstant())) {
            LocalDate predictionStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            Map<LocalDate, ConsumptionPrediction> existingPredictions = householdUtility
                .getConsumptionPredictions()
                .stream()
                .filter(cp -> !cp.getDate().isBefore(predictionStart))
                .collect(Collectors.toMap(ConsumptionPrediction::getDate, Function.identity()));
            List<ConsumptionPrediction> allPredictions = IntStream
                .range(0, 11)
                .mapToObj(predictionStart::plusMonths)
                .map(month ->
                    existingPredictions.computeIfAbsent(month, m -> new ConsumptionPrediction().date(m).householdUtility(householdUtility))
                )
                .toList();
            allPredictions.forEach(cp -> cp.setConsumption(householdUtility.getRate()));
            consumptionPredictionRepository.saveAll(allPredictions);
        }
    }
}
