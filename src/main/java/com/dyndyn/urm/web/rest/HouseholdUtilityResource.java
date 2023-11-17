package com.dyndyn.urm.web.rest;

import com.dyndyn.urm.repository.HouseholdUtilityRepository;
import com.dyndyn.urm.service.HouseholdUtilityService;
import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
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
 * REST controller for managing {@link com.dyndyn.urm.domain.HouseholdUtility}.
 */
@RestController
@RequestMapping("/api/household-utilities")
public class HouseholdUtilityResource {

    private final Logger log = LoggerFactory.getLogger(HouseholdUtilityResource.class);

    private static final String ENTITY_NAME = "householdUtility";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HouseholdUtilityService householdUtilityService;

    private final HouseholdUtilityRepository householdUtilityRepository;

    public HouseholdUtilityResource(
        HouseholdUtilityService householdUtilityService,
        HouseholdUtilityRepository householdUtilityRepository
    ) {
        this.householdUtilityService = householdUtilityService;
        this.householdUtilityRepository = householdUtilityRepository;
    }

    /**
     * {@code POST  /household-utilities} : Create a new householdUtility.
     *
     * @param householdUtilityDTO the householdUtilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new householdUtilityDTO, or with status {@code 400 (Bad Request)} if the householdUtility has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HouseholdUtilityDTO> createHouseholdUtility(@Valid @RequestBody HouseholdUtilityDTO householdUtilityDTO)
        throws URISyntaxException {
        log.debug("REST request to save HouseholdUtility : {}", householdUtilityDTO);
        if (householdUtilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new householdUtility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HouseholdUtilityDTO result = householdUtilityService.save(householdUtilityDTO);
        return ResponseEntity
            .created(new URI("/api/household-utilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /household-utilities/:id} : Updates an existing householdUtility.
     *
     * @param id the id of the householdUtilityDTO to save.
     * @param householdUtilityDTO the householdUtilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated householdUtilityDTO,
     * or with status {@code 400 (Bad Request)} if the householdUtilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the householdUtilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HouseholdUtilityDTO> updateHouseholdUtility(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HouseholdUtilityDTO householdUtilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HouseholdUtility : {}, {}", id, householdUtilityDTO);
        if (householdUtilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, householdUtilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!householdUtilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HouseholdUtilityDTO result = householdUtilityService.update(householdUtilityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, householdUtilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /household-utilities/:id} : Partial updates given fields of an existing householdUtility, field will ignore if it is null
     *
     * @param id the id of the householdUtilityDTO to save.
     * @param householdUtilityDTO the householdUtilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated householdUtilityDTO,
     * or with status {@code 400 (Bad Request)} if the householdUtilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the householdUtilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the householdUtilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HouseholdUtilityDTO> partialUpdateHouseholdUtility(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HouseholdUtilityDTO householdUtilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HouseholdUtility partially : {}, {}", id, householdUtilityDTO);
        if (householdUtilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, householdUtilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!householdUtilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HouseholdUtilityDTO> result = householdUtilityService.partialUpdate(householdUtilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, householdUtilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /household-utilities} : get all the householdUtilities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of householdUtilities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HouseholdUtilityDTO>> getAllHouseholdUtilities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of HouseholdUtilities");
        Page<HouseholdUtilityDTO> page;
        if (eagerload) {
            page = householdUtilityService.findAllWithEagerRelationships(pageable);
        } else {
            page = householdUtilityService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /household-utilities/:id} : get the "id" householdUtility.
     *
     * @param id the id of the householdUtilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the householdUtilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HouseholdUtilityDTO> getHouseholdUtility(@PathVariable Long id) {
        log.debug("REST request to get HouseholdUtility : {}", id);
        Optional<HouseholdUtilityDTO> householdUtilityDTO = householdUtilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(householdUtilityDTO);
    }

    /**
     * {@code DELETE  /household-utilities/:id} : delete the "id" householdUtility.
     *
     * @param id the id of the householdUtilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouseholdUtility(@PathVariable Long id) {
        log.debug("REST request to delete HouseholdUtility : {}", id);
        householdUtilityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
