package com.dyndyn.urm.service;

import com.dyndyn.urm.domain.HouseholdUtility;
import org.springframework.transaction.annotation.Transactional;

public interface ConsumptionPredictionService {
    @Transactional
    void generatePredictions(HouseholdUtility householdUtility);
}
