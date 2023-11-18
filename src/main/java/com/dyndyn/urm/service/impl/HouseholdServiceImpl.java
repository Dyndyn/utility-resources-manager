package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.Household;
import com.dyndyn.urm.repository.HouseholdRepository;
import com.dyndyn.urm.security.SecurityUtils;
import com.dyndyn.urm.service.HouseholdService;
import com.dyndyn.urm.service.dto.HouseholdDTO;
import com.dyndyn.urm.service.mapper.HouseholdMapper;
import java.util.Optional;
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
        return householdRepository.findOneWithEagerRelationships(id).map(householdMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Household : {}", id);
        householdRepository.deleteById(id);
    }
}
