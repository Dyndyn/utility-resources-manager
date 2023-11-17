package com.dyndyn.urm.service;

import com.dyndyn.urm.service.dto.UtilityProviderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dyndyn.urm.domain.UtilityProvider}.
 */
public interface UtilityProviderService {
    /**
     * Save a utilityProvider.
     *
     * @param utilityProviderDTO the entity to save.
     * @return the persisted entity.
     */
    UtilityProviderDTO save(UtilityProviderDTO utilityProviderDTO);

    /**
     * Updates a utilityProvider.
     *
     * @param utilityProviderDTO the entity to update.
     * @return the persisted entity.
     */
    UtilityProviderDTO update(UtilityProviderDTO utilityProviderDTO);

    /**
     * Partially updates a utilityProvider.
     *
     * @param utilityProviderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UtilityProviderDTO> partialUpdate(UtilityProviderDTO utilityProviderDTO);

    /**
     * Get all the utilityProviders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UtilityProviderDTO> findAll(Pageable pageable);

    /**
     * Get all the utilityProviders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UtilityProviderDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" utilityProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UtilityProviderDTO> findOne(Long id);

    /**
     * Delete the "id" utilityProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
