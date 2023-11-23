package com.dyndyn.urm.web.rest;

import com.dyndyn.urm.service.ConsumptionPredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.dyndyn.urm.domain.ConsumptionHistory}.
 */
@RestController
public class ConsumptionPredictionResource {

    private final Logger log = LoggerFactory.getLogger(ConsumptionPredictionResource.class);

    private final ConsumptionPredictionService consumptionPredictionService;

    public ConsumptionPredictionResource(ConsumptionPredictionService consumptionPredictionService) {
        this.consumptionPredictionService = consumptionPredictionService;
    }

    @PostMapping("/api/household-utilities/{householdUtilityId}/consumption-predictions")
    public ResponseEntity<Object> generateConsumptionPredictions(@PathVariable Long householdUtilityId) {
        log.debug("Generating predictions for household utility : {}", householdUtilityId);
        consumptionPredictionService.generatePredictions(householdUtilityId);
        return ResponseEntity.noContent().build();
    }
}
