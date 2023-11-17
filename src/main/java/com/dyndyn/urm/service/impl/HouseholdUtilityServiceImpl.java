package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.repository.HouseholdUtilityRepository;
import com.dyndyn.urm.service.HouseholdUtilityService;
import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
import com.dyndyn.urm.service.mapper.HouseholdUtilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dyndyn.urm.domain.HouseholdUtility}.
 */
@Service
@Transactional
public class HouseholdUtilityServiceImpl implements HouseholdUtilityService {

    private final Logger log = LoggerFactory.getLogger(HouseholdUtilityServiceImpl.class);

    private final HouseholdUtilityRepository householdUtilityRepository;

    private final HouseholdUtilityMapper householdUtilityMapper;

    public HouseholdUtilityServiceImpl(
        HouseholdUtilityRepository householdUtilityRepository,
        HouseholdUtilityMapper householdUtilityMapper
    ) {
        this.householdUtilityRepository = householdUtilityRepository;
        this.householdUtilityMapper = householdUtilityMapper;
    }

    @Override
    public HouseholdUtilityDTO save(HouseholdUtilityDTO householdUtilityDTO) {
        log.debug("Request to save HouseholdUtility : {}", householdUtilityDTO);
        HouseholdUtility householdUtility = householdUtilityMapper.toEntity(householdUtilityDTO);
        householdUtility = householdUtilityRepository.save(householdUtility);
        return householdUtilityMapper.toDto(householdUtility);
    }

    @Override
    public HouseholdUtilityDTO update(HouseholdUtilityDTO householdUtilityDTO) {
        log.debug("Request to update HouseholdUtility : {}", householdUtilityDTO);
        HouseholdUtility householdUtility = householdUtilityMapper.toEntity(householdUtilityDTO);
        householdUtility = householdUtilityRepository.save(householdUtility);
        return householdUtilityMapper.toDto(householdUtility);
    }

    @Override
    public Optional<HouseholdUtilityDTO> partialUpdate(HouseholdUtilityDTO householdUtilityDTO) {
        log.debug("Request to partially update HouseholdUtility : {}", householdUtilityDTO);

        return householdUtilityRepository
            .findById(householdUtilityDTO.getId())
            .map(existingHouseholdUtility -> {
                householdUtilityMapper.partialUpdate(existingHouseholdUtility, householdUtilityDTO);

                return existingHouseholdUtility;
            })
            .map(householdUtilityRepository::save)
            .map(householdUtilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HouseholdUtilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HouseholdUtilities");
        return householdUtilityRepository.findAll(pageable).map(householdUtilityMapper::toDto);
    }

    public Page<HouseholdUtilityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return householdUtilityRepository.findAllWithEagerRelationships(pageable).map(householdUtilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HouseholdUtilityDTO> findOne(Long id) {
        log.debug("Request to get HouseholdUtility : {}", id);
        return householdUtilityRepository.findOneWithEagerRelationships(id).map(householdUtilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HouseholdUtility : {}", id);
        householdUtilityRepository.deleteById(id);
    }
}
