package com.dyndyn.urm.service;

import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dyndyn.urm.domain.HouseholdUtility}.
 */
public interface HouseholdUtilityService {
    /**
     * Save a householdUtility.
     *
     * @param householdUtilityDTO the entity to save.
     * @return the persisted entity.
     */
    HouseholdUtilityDTO save(HouseholdUtilityDTO householdUtilityDTO);

    /**
     * Updates a householdUtility.
     *
     * @param householdUtilityDTO the entity to update.
     * @return the persisted entity.
     */
    HouseholdUtilityDTO update(HouseholdUtilityDTO householdUtilityDTO);

    /**
     * Partially updates a householdUtility.
     *
     * @param householdUtilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HouseholdUtilityDTO> partialUpdate(HouseholdUtilityDTO householdUtilityDTO);

    /**
     * Get all the householdUtilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HouseholdUtilityDTO> findAll(Pageable pageable);

    /**
     * Get all the householdUtilities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HouseholdUtilityDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" householdUtility.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HouseholdUtilityDTO> findOne(Long id);

    /**
     * Delete the "id" householdUtility.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
