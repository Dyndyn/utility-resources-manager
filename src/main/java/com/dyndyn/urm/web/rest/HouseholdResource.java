package com.dyndyn.urm.web.rest;

import com.dyndyn.urm.repository.HouseholdRepository;
import com.dyndyn.urm.service.HouseholdService;
import com.dyndyn.urm.service.dto.HouseholdDTO;
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
 * REST controller for managing {@link com.dyndyn.urm.domain.Household}.
 */
@RestController
@RequestMapping("/api/households")
public class HouseholdResource {

    private final Logger log = LoggerFactory.getLogger(HouseholdResource.class);

    private static final String ENTITY_NAME = "household";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HouseholdService householdService;

    private final HouseholdRepository householdRepository;

    public HouseholdResource(HouseholdService householdService, HouseholdRepository householdRepository) {
        this.householdService = householdService;
        this.householdRepository = householdRepository;
    }

    /**
     * {@code POST  /households} : Create a new household.
     *
     * @param householdDTO the householdDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new householdDTO, or with status {@code 400 (Bad Request)} if the household has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HouseholdDTO> createHousehold(@Valid @RequestBody HouseholdDTO householdDTO) throws URISyntaxException {
        log.debug("REST request to save Household : {}", householdDTO);
        if (householdDTO.getId() != null) {
            throw new BadRequestAlertException("A new household cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HouseholdDTO result = householdService.save(householdDTO);
        return ResponseEntity
            .created(new URI("/api/households/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /households/:id} : Updates an existing household.
     *
     * @param id the id of the householdDTO to save.
     * @param householdDTO the householdDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated householdDTO,
     * or with status {@code 400 (Bad Request)} if the householdDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the householdDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HouseholdDTO> updateHousehold(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HouseholdDTO householdDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Household : {}, {}", id, householdDTO);
        if (householdDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, householdDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!householdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HouseholdDTO result = householdService.update(householdDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, householdDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /households/:id} : Partial updates given fields of an existing household, field will ignore if it is null
     *
     * @param id the id of the householdDTO to save.
     * @param householdDTO the householdDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated householdDTO,
     * or with status {@code 400 (Bad Request)} if the householdDTO is not valid,
     * or with status {@code 404 (Not Found)} if the householdDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the householdDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HouseholdDTO> partialUpdateHousehold(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HouseholdDTO householdDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Household partially : {}, {}", id, householdDTO);
        if (householdDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, householdDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!householdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HouseholdDTO> result = householdService.partialUpdate(householdDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, householdDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /households} : get all the households.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of households in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HouseholdDTO>> getAllHouseholds(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Households");
        Page<HouseholdDTO> page;
        if (eagerload) {
            page = householdService.findAllWithEagerRelationships(pageable);
        } else {
            page = householdService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /households/:id} : get the "id" household.
     *
     * @param id the id of the householdDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the householdDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HouseholdDTO> getHousehold(@PathVariable Long id) {
        log.debug("REST request to get Household : {}", id);
        Optional<HouseholdDTO> householdDTO = householdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(householdDTO);
    }

    /**
     * {@code DELETE  /households/:id} : delete the "id" household.
     *
     * @param id the id of the householdDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousehold(@PathVariable Long id) {
        log.debug("REST request to delete Household : {}", id);
        householdService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
