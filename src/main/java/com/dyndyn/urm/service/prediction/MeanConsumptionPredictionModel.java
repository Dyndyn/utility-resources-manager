package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(3)
public class MeanConsumptionPredictionModel implements PredictionModel {

    private final Logger log = LoggerFactory.getLogger(MeanConsumptionPredictionModel.class);
    private final ConsumptionHistoryRepository consumptionHistoryRepository;

    public MeanConsumptionPredictionModel(ConsumptionHistoryRepository consumptionHistoryRepository) {
        this.consumptionHistoryRepository = consumptionHistoryRepository;
    }

    @Override
    public BigDecimal predict(HouseholdUtility householdUtility, LocalDate date) {
        return consumptionHistoryRepository.findMeanConsumptionByHouseholdUtilityId(householdUtility.getId());
    }

    @Override
    public boolean support(HouseholdUtility householdUtility) {
        return (
            !householdUtility.getUtilityProvider().getUtility().getConstant() &&
            !householdUtility.getUtilityProvider().getUtility().getPredictable()
        );
    }
}
