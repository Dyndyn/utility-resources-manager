package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.repository.UtilityRepository;
import com.dyndyn.urm.service.UtilityService;
import com.dyndyn.urm.service.dto.UtilityDTO;
import com.dyndyn.urm.service.mapper.UtilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dyndyn.urm.domain.Utility}.
 */
@Service
@Transactional
public class UtilityServiceImpl implements UtilityService {

    private final Logger log = LoggerFactory.getLogger(UtilityServiceImpl.class);

    private final UtilityRepository utilityRepository;

    private final UtilityMapper utilityMapper;

    public UtilityServiceImpl(UtilityRepository utilityRepository, UtilityMapper utilityMapper) {
        this.utilityRepository = utilityRepository;
        this.utilityMapper = utilityMapper;
    }

    @Override
    public UtilityDTO save(UtilityDTO utilityDTO) {
        log.debug("Request to save Utility : {}", utilityDTO);
        Utility utility = utilityMapper.toEntity(utilityDTO);
        utility = utilityRepository.save(utility);
        return utilityMapper.toDto(utility);
    }

    @Override
    public UtilityDTO update(UtilityDTO utilityDTO) {
        log.debug("Request to update Utility : {}", utilityDTO);
        Utility utility = utilityMapper.toEntity(utilityDTO);
        utility = utilityRepository.save(utility);
        return utilityMapper.toDto(utility);
    }

    @Override
    public Optional<UtilityDTO> partialUpdate(UtilityDTO utilityDTO) {
        log.debug("Request to partially update Utility : {}", utilityDTO);

        return utilityRepository
            .findById(utilityDTO.getId())
            .map(existingUtility -> {
                utilityMapper.partialUpdate(existingUtility, utilityDTO);

                return existingUtility;
            })
            .map(utilityRepository::save)
            .map(utilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UtilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Utilities");
        return utilityRepository.findAll(pageable).map(utilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UtilityDTO> findOne(Long id) {
        log.debug("Request to get Utility : {}", id);
        return utilityRepository.findById(id).map(utilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Utility : {}", id);
        utilityRepository.deleteById(id);
    }
}
