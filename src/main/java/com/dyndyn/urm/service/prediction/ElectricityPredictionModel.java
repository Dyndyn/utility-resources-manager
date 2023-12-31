package com.dyndyn.urm.service.prediction;

import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.repository.ConsumptionPredictionRepository;
import com.dyndyn.urm.repository.TemperatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class ElectricityPredictionModel extends RandomForestPredictionModel {

    private final Logger log = LoggerFactory.getLogger(ElectricityPredictionModel.class);

    public ElectricityPredictionModel(
        ConsumptionHistoryRepository consumptionHistoryRepository,
        ConsumptionPredictionRepository consumptionPredictionRepository,
        TemperatureRepository temperatureRepository
    ) {
        super(1L, consumptionHistoryRepository, consumptionPredictionRepository, temperatureRepository);
    }
}
