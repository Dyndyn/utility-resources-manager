package com.dyndyn.urm.service;

import com.dyndyn.urm.service.dto.UtilityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dyndyn.urm.domain.Utility}.
 */
public interface UtilityService {
    /**
     * Save a utility.
     *
     * @param utilityDTO the entity to save.
     * @return the persisted entity.
     */
    UtilityDTO save(UtilityDTO utilityDTO);

    /**
     * Updates a utility.
     *
     * @param utilityDTO the entity to update.
     * @return the persisted entity.
     */
    UtilityDTO update(UtilityDTO utilityDTO);

    /**
     * Partially updates a utility.
     *
     * @param utilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UtilityDTO> partialUpdate(UtilityDTO utilityDTO);

    /**
     * Get all the utilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UtilityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" utility.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UtilityDTO> findOne(Long id);

    /**
     * Delete the "id" utility.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
