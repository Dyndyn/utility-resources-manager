package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.domain.HouseholdUtility;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class PredictionContext {

    private final List<PredictionModel> predictionModels;

    public PredictionContext(List<PredictionModel> predictionModels) {
        this.predictionModels = predictionModels;
    }

    public PredictionModel getPredictionModel(HouseholdUtility householdUtility) {
        return predictionModels
            .stream()
            .filter(pm -> pm.support(householdUtility))
            .findFirst()
            .orElseThrow(() ->
                new NoSuchElementException("Prediction model is not found for household utility [%s]".formatted(householdUtility.getId()))
            );
    }
}
