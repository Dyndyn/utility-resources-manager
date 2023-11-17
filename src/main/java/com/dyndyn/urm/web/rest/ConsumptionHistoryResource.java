package com.dyndyn.urm.web.rest;

import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.service.ConsumptionHistoryService;
import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import com.dyndyn.urm.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dyndyn.urm.domain.ConsumptionHistory}.
 */
@RestController
@RequestMapping("/api/consumption-histories")
public class ConsumptionHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ConsumptionHistoryResource.class);

    private static final String ENTITY_NAME = "consumptionHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsumptionHistoryService consumptionHistoryService;

    private final ConsumptionHistoryRepository consumptionHistoryRepository;

    public ConsumptionHistoryResource(
        ConsumptionHistoryService consumptionHistoryService,
        ConsumptionHistoryRepository consumptionHistoryRepository
    ) {
        this.consumptionHistoryService = consumptionHistoryService;
        this.consumptionHistoryRepository = consumptionHistoryRepository;
    }

    /**
     * {@code POST  /consumption-histories} : Create a new consumptionHistory.
     *
     * @param consumptionHistoryDTO the consumptionHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consumptionHistoryDTO, or with status {@code 400 (Bad Request)} if the consumptionHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConsumptionHistoryDTO> createConsumptionHistory(@Valid @RequestBody ConsumptionHistoryDTO consumptionHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ConsumptionHistory : {}", consumptionHistoryDTO);
        if (consumptionHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new consumptionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsumptionHistoryDTO result = consumptionHistoryService.save(consumptionHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/consumption-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consumption-histories/:id} : Updates an existing consumptionHistory.
     *
     * @param id the id of the consumptionHistoryDTO to save.
     * @param consumptionHistoryDTO the consumptionHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumptionHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the consumptionHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consumptionHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsumptionHistoryDTO> updateConsumptionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConsumptionHistoryDTO consumptionHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ConsumptionHistory : {}, {}", id, consumptionHistoryDTO);
        if (consumptionHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumptionHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumptionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsumptionHistoryDTO result = consumptionHistoryService.update(consumptionHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consumptionHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consumption-histories/:id} : Partial updates given fields of an existing consumptionHistory, field will ignore if it is null
     *
     * @param id the id of the consumptionHistoryDTO to save.
     * @param consumptionHistoryDTO the consumptionHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumptionHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the consumptionHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consumptionHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consumptionHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsumptionHistoryDTO> partialUpdateConsumptionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConsumptionHistoryDTO consumptionHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConsumptionHistory partially : {}, {}", id, consumptionHistoryDTO);
        if (consumptionHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumptionHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumptionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsumptionHistoryDTO> result = consumptionHistoryService.partialUpdate(consumptionHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consumptionHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consumption-histories} : get all the consumptionHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consumptionHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConsumptionHistoryDTO>> getAllConsumptionHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ConsumptionHistories");
        Page<ConsumptionHistoryDTO> page;
        if (eagerload) {
            page = consumptionHistoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = consumptionHistoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consumption-histories/:id} : get the "id" consumptionHistory.
     *
     * @param id the id of the consumptionHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consumptionHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsumptionHistoryDTO> getConsumptionHistory(@PathVariable Long id) {
        log.debug("REST request to get ConsumptionHistory : {}", id);
        Optional<ConsumptionHistoryDTO> consumptionHistoryDTO = consumptionHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consumptionHistoryDTO);
    }

    /**
     * {@code DELETE  /consumption-histories/:id} : delete the "id" consumptionHistory.
     *
     * @param id the id of the consumptionHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsumptionHistory(@PathVariable Long id) {
        log.debug("REST request to delete ConsumptionHistory : {}", id);
        consumptionHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
