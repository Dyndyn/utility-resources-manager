package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.repository.HouseholdUtilityRepository;
import com.dyndyn.urm.service.ConsumptionHistoryService;
import com.dyndyn.urm.service.ConsumptionPredictionService;
import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import com.dyndyn.urm.service.mapper.ConsumptionHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dyndyn.urm.domain.ConsumptionHistory}.
 */
@Service
@Transactional
public class ConsumptionHistoryServiceImpl implements ConsumptionHistoryService {

    private final Logger log = LoggerFactory.getLogger(ConsumptionHistoryServiceImpl.class);

    private final ConsumptionHistoryRepository consumptionHistoryRepository;
    private final ConsumptionPredictionService consumptionPredictionService;
    private final HouseholdUtilityRepository householdUtilityRepository;

    private final ConsumptionHistoryMapper consumptionHistoryMapper;

    public ConsumptionHistoryServiceImpl(
        ConsumptionHistoryRepository consumptionHistoryRepository,
        ConsumptionPredictionService consumptionPredictionService,
        HouseholdUtilityRepository householdUtilityRepository,
        ConsumptionHistoryMapper consumptionHistoryMapper
    ) {
        this.consumptionHistoryRepository = consumptionHistoryRepository;
        this.consumptionPredictionService = consumptionPredictionService;
        this.householdUtilityRepository = householdUtilityRepository;
        this.consumptionHistoryMapper = consumptionHistoryMapper;
    }

    @Override
    public ConsumptionHistoryDTO save(ConsumptionHistoryDTO consumptionHistoryDTO) {
        log.debug("Request to save ConsumptionHistory : {}", consumptionHistoryDTO);
        ConsumptionHistory consumptionHistory = consumptionHistoryMapper.toEntity(consumptionHistoryDTO);
        consumptionHistory.setDate(consumptionHistory.getDate().withDayOfMonth(1));
        HouseholdUtility householdUtility = householdUtilityRepository.findById(consumptionHistoryDTO.getHouseholdUtility().getId()).get();
        consumptionHistory.setCost(consumptionHistory.getConsumption().multiply(householdUtility.getRate()));
        consumptionHistory = consumptionHistoryRepository.save(consumptionHistory);
        consumptionPredictionService.generatePredictions(consumptionHistory.getHouseholdUtility().getId());
        return consumptionHistoryMapper.toDto(consumptionHistory);
    }

    @Override
    public ConsumptionHistoryDTO update(ConsumptionHistoryDTO consumptionHistoryDTO) {
        log.debug("Request to update ConsumptionHistory : {}", consumptionHistoryDTO);
        ConsumptionHistory consumptionHistory = consumptionHistoryMapper.toEntity(consumptionHistoryDTO);
        consumptionHistory = consumptionHistoryRepository.save(consumptionHistory);
        return consumptionHistoryMapper.toDto(consumptionHistory);
    }

    @Override
    public Optional<ConsumptionHistoryDTO> partialUpdate(ConsumptionHistoryDTO consumptionHistoryDTO) {
        log.debug("Request to partially update ConsumptionHistory : {}", consumptionHistoryDTO);

        return consumptionHistoryRepository
            .findById(consumptionHistoryDTO.getId())
            .map(existingConsumptionHistory -> {
                consumptionHistoryMapper.partialUpdate(existingConsumptionHistory, consumptionHistoryDTO);

                return existingConsumptionHistory;
            })
            .map(consumptionHistoryRepository::save)
            .map(consumptionHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsumptionHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConsumptionHistories");
        return consumptionHistoryRepository.findAll(pageable).map(consumptionHistoryMapper::toDto);
    }

    public Page<ConsumptionHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return consumptionHistoryRepository.findAllWithEagerRelationships(pageable).map(consumptionHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsumptionHistoryDTO> findOne(Long id) {
        log.debug("Request to get ConsumptionHistory : {}", id);
        return consumptionHistoryRepository.findOneWithEagerRelationships(id).map(consumptionHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConsumptionHistory : {}", id);
        consumptionHistoryRepository.deleteById(id);
    }
}
