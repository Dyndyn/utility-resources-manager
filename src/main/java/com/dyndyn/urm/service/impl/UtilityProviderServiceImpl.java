package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.UtilityProvider;
import com.dyndyn.urm.repository.UtilityProviderRepository;
import com.dyndyn.urm.service.UtilityProviderService;
import com.dyndyn.urm.service.dto.UtilityProviderDTO;
import com.dyndyn.urm.service.mapper.UtilityProviderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dyndyn.urm.domain.UtilityProvider}.
 */
@Service
@Transactional
public class UtilityProviderServiceImpl implements UtilityProviderService {

    private final Logger log = LoggerFactory.getLogger(UtilityProviderServiceImpl.class);

    private final UtilityProviderRepository utilityProviderRepository;

    private final UtilityProviderMapper utilityProviderMapper;

    public UtilityProviderServiceImpl(UtilityProviderRepository utilityProviderRepository, UtilityProviderMapper utilityProviderMapper) {
        this.utilityProviderRepository = utilityProviderRepository;
        this.utilityProviderMapper = utilityProviderMapper;
    }

    @Override
    public UtilityProviderDTO save(UtilityProviderDTO utilityProviderDTO) {
        log.debug("Request to save UtilityProvider : {}", utilityProviderDTO);
        UtilityProvider utilityProvider = utilityProviderMapper.toEntity(utilityProviderDTO);
        utilityProvider = utilityProviderRepository.save(utilityProvider);
        return utilityProviderMapper.toDto(utilityProvider);
    }

    @Override
    public UtilityProviderDTO update(UtilityProviderDTO utilityProviderDTO) {
        log.debug("Request to update UtilityProvider : {}", utilityProviderDTO);
        UtilityProvider utilityProvider = utilityProviderMapper.toEntity(utilityProviderDTO);
        utilityProvider = utilityProviderRepository.save(utilityProvider);
        return utilityProviderMapper.toDto(utilityProvider);
    }

    @Override
    public Optional<UtilityProviderDTO> partialUpdate(UtilityProviderDTO utilityProviderDTO) {
        log.debug("Request to partially update UtilityProvider : {}", utilityProviderDTO);

        return utilityProviderRepository
            .findById(utilityProviderDTO.getId())
            .map(existingUtilityProvider -> {
                utilityProviderMapper.partialUpdate(existingUtilityProvider, utilityProviderDTO);

                return existingUtilityProvider;
            })
            .map(utilityProviderRepository::save)
            .map(utilityProviderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UtilityProviderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UtilityProviders");
        return utilityProviderRepository.findAll(pageable).map(utilityProviderMapper::toDto);
    }

    public Page<UtilityProviderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return utilityProviderRepository.findAllWithEagerRelationships(pageable).map(utilityProviderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UtilityProviderDTO> findOne(Long id) {
        log.debug("Request to get UtilityProvider : {}", id);
        return utilityProviderRepository.findOneWithEagerRelationships(id).map(utilityProviderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UtilityProvider : {}", id);
        utilityProviderRepository.deleteById(id);
    }
}
