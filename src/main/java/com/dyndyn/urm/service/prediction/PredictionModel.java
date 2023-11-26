package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.domain.HouseholdUtility;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface PredictionModel {
    BigDecimal predict(HouseholdUtility householdUtility, LocalDate date);
    boolean support(HouseholdUtility householdUtility);
}
