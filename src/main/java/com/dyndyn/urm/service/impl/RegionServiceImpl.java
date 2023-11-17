package com.dyndyn.urm.service.impl;

import com.dyndyn.urm.domain.Region;
import com.dyndyn.urm.repository.RegionRepository;
import com.dyndyn.urm.service.RegionService;
import com.dyndyn.urm.service.dto.RegionDTO;
import com.dyndyn.urm.service.mapper.RegionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dyndyn.urm.domain.Region}.
 */
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final RegionRepository regionRepository;

    private final RegionMapper regionMapper;

    public RegionServiceImpl(RegionRepository regionRepository, RegionMapper regionMapper) {
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
    }

    @Override
    public RegionDTO save(RegionDTO regionDTO) {
        log.debug("Request to save Region : {}", regionDTO);
        Region region = regionMapper.toEntity(regionDTO);
        region = regionRepository.save(region);
        return regionMapper.toDto(region);
    }

    @Override
    public RegionDTO update(RegionDTO regionDTO) {
        log.debug("Request to update Region : {}", regionDTO);
        Region region = regionMapper.toEntity(regionDTO);
        region = regionRepository.save(region);
        return regionMapper.toDto(region);
    }

    @Override
    public Optional<RegionDTO> partialUpdate(RegionDTO regionDTO) {
        log.debug("Request to partially update Region : {}", regionDTO);

        return regionRepository
            .findById(regionDTO.getId())
            .map(existingRegion -> {
                regionMapper.partialUpdate(existingRegion, regionDTO);

                return existingRegion;
            })
            .map(regionRepository::save)
            .map(regionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regions");
        return regionRepository.findAll(pageable).map(regionMapper::toDto);
    }

    public Page<RegionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return regionRepository.findAllWithEagerRelationships(pageable).map(regionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegionDTO> findOne(Long id) {
        log.debug("Request to get Region : {}", id);
        return regionRepository.findOneWithEagerRelationships(id).map(regionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Region : {}", id);
        regionRepository.deleteById(id);
    }
}
