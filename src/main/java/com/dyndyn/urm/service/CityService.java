package com.dyndyn.urm.service;

import com.dyndyn.urm.service.dto.CityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dyndyn.urm.domain.City}.
 */
public interface CityService {
    /**
     * Save a city.
     *
     * @param cityDTO the entity to save.
     * @return the persisted entity.
     */
    CityDTO save(CityDTO cityDTO);

    /**
     * Updates a city.
     *
     * @param cityDTO the entity to update.
     * @return the persisted entity.
     */
    CityDTO update(CityDTO cityDTO);

    /**
     * Partially updates a city.
     *
     * @param cityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CityDTO> partialUpdate(CityDTO cityDTO);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CityDTO> findAll(Pageable pageable);

    /**
     * Get all the cities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CityDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" city.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CityDTO> findOne(Long id);

    /**
     * Delete the "id" city.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
