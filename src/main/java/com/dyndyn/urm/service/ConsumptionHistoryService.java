package com.dyndyn.urm.service;

import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dyndyn.urm.domain.ConsumptionHistory}.
 */
public interface ConsumptionHistoryService {
    /**
     * Save a consumptionHistory.
     *
     * @param consumptionHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    ConsumptionHistoryDTO save(ConsumptionHistoryDTO consumptionHistoryDTO);

    /**
     * Updates a consumptionHistory.
     *
     * @param consumptionHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    ConsumptionHistoryDTO update(ConsumptionHistoryDTO consumptionHistoryDTO);

    /**
     * Partially updates a consumptionHistory.
     *
     * @param consumptionHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConsumptionHistoryDTO> partialUpdate(ConsumptionHistoryDTO consumptionHistoryDTO);

    /**
     * Get all the consumptionHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConsumptionHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" consumptionHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConsumptionHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" consumptionHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
