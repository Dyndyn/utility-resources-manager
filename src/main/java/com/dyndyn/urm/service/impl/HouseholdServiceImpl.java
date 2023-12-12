package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.ConsumptionPrediction;
import com.dyndyn.urm.domain.Household;
import com.dyndyn.urm.repository.HouseholdRepository;
import com.dyndyn.urm.security.SecurityUtils;
import com.dyndyn.urm.service.HouseholdService;
import com.dyndyn.urm.service.dto.GraphDataDTO;
import com.dyndyn.urm.service.dto.HouseholdDTO;
import com.dyndyn.urm.service.mapper.HouseholdMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dyndyn.urm.domain.Household}.
 */
@Service
@Transactional
public class HouseholdServiceImpl implements HouseholdService {

    private final Logger log = LoggerFactory.getLogger(HouseholdServiceImpl.class);

    private final HouseholdRepository householdRepository;

    private final HouseholdMapper householdMapper;

    public HouseholdServiceImpl(HouseholdRepository householdRepository, HouseholdMapper householdMapper) {
        this.householdRepository = householdRepository;
        this.householdMapper = householdMapper;
    }

    @Override
    public HouseholdDTO save(HouseholdDTO householdDTO) {
        log.debug("Request to save Household : {}", householdDTO);
        Household household = householdMapper.toEntity(householdDTO);
        household = householdRepository.save(household);
        return householdMapper.toDto(household);
    }

    @Override
    public HouseholdDTO update(HouseholdDTO householdDTO) {
        log.debug("Request to update Household : {}", householdDTO);
        Household household = householdMapper.toEntity(householdDTO);
        household = householdRepository.save(household);
        return householdMapper.toDto(household);
    }

    @Override
    public Optional<HouseholdDTO> partialUpdate(HouseholdDTO householdDTO) {
        log.debug("Request to partially update Household : {}", householdDTO);

        return householdRepository
            .findById(householdDTO.getId())
            .map(existingHousehold -> {
                householdMapper.partialUpdate(existingHousehold, householdDTO);

                return existingHousehold;
            })
            .map(householdRepository::save)
            .map(householdMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HouseholdDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Households");
        return householdRepository.findAll(pageable).map(householdMapper::toDto);
    }

    public Page<HouseholdDTO> findAllWithEagerRelationships(Pageable pageable) {
        return householdRepository
            .findAllWithEagerRelationships(pageable, SecurityUtils.getCurrentUserLoginOrThrow())
            .map(householdMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HouseholdDTO> findOne(Long id) {
        log.debug("Request to get Household : {}", id);
        return householdRepository
            .findOneWithEagerRelationships(id)
            .map(s -> {
                HouseholdDTO dto = householdMapper.toDto(s);
                Map<LocalDate, BigDecimal> monthlyCosts = s
                    .getHouseholdUtilities()
                    .stream()
                    .flatMap(hu -> hu.getConsumptionHistories().stream())
                    .collect(
                        Collectors.groupingBy(
                            ConsumptionHistory::getDate,
                            Collectors.mapping(ConsumptionHistory::getCost, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                        )
                    );
                Map<LocalDate, BigDecimal> monthlyCostPrediction = s
                    .getHouseholdUtilities()
                    .stream()
                    .flatMap(hu -> hu.getConsumptionPredictions().stream())
                    .collect(
                        Collectors.groupingBy(
                            ConsumptionPrediction::getDate,
                            Collectors.mapping(
                                cp -> cp.getConsumption().multiply(cp.getHouseholdUtility().getRate()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                            )
                        )
                    );
                dto.setCosts(
                    new GraphDataDTO(
                        monthlyCosts.keySet().stream().sorted().toList(),
                        monthlyCosts.entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).map(Entry::getValue).toList()
                    )
                );
                dto.setPredictedCosts(
                    new GraphDataDTO(
                        monthlyCostPrediction.keySet().stream().sorted().toList(),
                        monthlyCostPrediction.entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).map(Entry::getValue).toList()
                    )
                );
                return dto;
            });
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Household : {}", id);
        householdRepository.deleteById(id);
    }
}
