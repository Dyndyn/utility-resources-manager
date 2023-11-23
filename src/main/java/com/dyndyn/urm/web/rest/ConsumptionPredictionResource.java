package com.dyndyn.urm.web.rest;

import com.dyndyn.urm.service.prediction.ElectricityPredictionModel;
import java.net.URISyntaxException;
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

    private final ElectricityPredictionModel electricityPredictionModel;

    public ConsumptionPredictionResource(ElectricityPredictionModel electricityPredictionModel) {
        this.electricityPredictionModel = electricityPredictionModel;
    }

    @PostMapping("/api/utilities/{utilityId}/consumption-predictions")
    public ResponseEntity<Object> createConsumptionHistory(@PathVariable Integer utilityId) {
        log.debug("Generating predictions for utility : {}", utilityId);
        electricityPredictionModel.trainAndUseModel();
        return ResponseEntity.noContent().build();
    }
}
