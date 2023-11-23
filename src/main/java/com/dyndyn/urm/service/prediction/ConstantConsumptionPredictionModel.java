package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.domain.HouseholdUtility;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(4)
public class ConstantConsumptionPredictionModel implements PredictionModel {

    @Override
    public BigDecimal predict(HouseholdUtility householdUtility, LocalDate date) {
        return BigDecimal.ONE;
    }

    @Override
    public boolean support(HouseholdUtility householdUtility) {
        return householdUtility.getUtilityProvider().getUtility().getConstant();
    }
}
