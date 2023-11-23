package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.repository.ConsumptionPredictionRepository;
import com.dyndyn.urm.repository.TemperatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class GasPredictionModel extends RandomForestPredictionModel {

    private final Logger log = LoggerFactory.getLogger(GasPredictionModel.class);

    public GasPredictionModel(
        ConsumptionHistoryRepository consumptionHistoryRepository,
        ConsumptionPredictionRepository consumptionPredictionRepository,
        TemperatureRepository temperatureRepository
    ) {
        super(2L, consumptionHistoryRepository, consumptionPredictionRepository, temperatureRepository);
    }
}
