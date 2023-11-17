package com.dyndyn.urm.web.rest;

import com.dyndyn.urm.repository.UtilityProviderRepository;
import com.dyndyn.urm.service.UtilityProviderService;
import com.dyndyn.urm.service.dto.UtilityProviderDTO;
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
 * REST controller for managing {@link com.dyndyn.urm.domain.UtilityProvider}.
 */
@RestController
@RequestMapping("/api/utility-providers")
public class UtilityProviderResource {

    private final Logger log = LoggerFactory.getLogger(UtilityProviderResource.class);

    private static final String ENTITY_NAME = "utilityProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilityProviderService utilityProviderService;

    private final UtilityProviderRepository utilityProviderRepository;

    public UtilityProviderResource(UtilityProviderService utilityProviderService, UtilityProviderRepository utilityProviderRepository) {
        this.utilityProviderService = utilityProviderService;
        this.utilityProviderRepository = utilityProviderRepository;
    }

    /**
     * {@code POST  /utility-providers} : Create a new utilityProvider.
     *
     * @param utilityProviderDTO the utilityProviderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilityProviderDTO, or with status {@code 400 (Bad Request)} if the utilityProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UtilityProviderDTO> createUtilityProvider(@Valid @RequestBody UtilityProviderDTO utilityProviderDTO)
        throws URISyntaxException {
        log.debug("REST request to save UtilityProvider : {}", utilityProviderDTO);
        if (utilityProviderDTO.getId() != null) {
            throw new BadRequestAlertException("A new utilityProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UtilityProviderDTO result = utilityProviderService.save(utilityProviderDTO);
        return ResponseEntity
            .created(new URI("/api/utility-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utility-providers/:id} : Updates an existing utilityProvider.
     *
     * @param id the id of the utilityProviderDTO to save.
     * @param utilityProviderDTO the utilityProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilityProviderDTO,
     * or with status {@code 400 (Bad Request)} if the utilityProviderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilityProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtilityProviderDTO> updateUtilityProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UtilityProviderDTO utilityProviderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UtilityProvider : {}, {}", id, utilityProviderDTO);
        if (utilityProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilityProviderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilityProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UtilityProviderDTO result = utilityProviderService.update(utilityProviderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilityProviderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utility-providers/:id} : Partial updates given fields of an existing utilityProvider, field will ignore if it is null
     *
     * @param id the id of the utilityProviderDTO to save.
     * @param utilityProviderDTO the utilityProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilityProviderDTO,
     * or with status {@code 400 (Bad Request)} if the utilityProviderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the utilityProviderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilityProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtilityProviderDTO> partialUpdateUtilityProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UtilityProviderDTO utilityProviderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UtilityProvider partially : {}, {}", id, utilityProviderDTO);
        if (utilityProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilityProviderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilityProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtilityProviderDTO> result = utilityProviderService.partialUpdate(utilityProviderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilityProviderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /utility-providers} : get all the utilityProviders.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilityProviders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UtilityProviderDTO>> getAllUtilityProviders(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UtilityProviders");
        Page<UtilityProviderDTO> page;
        if (eagerload) {
            page = utilityProviderService.findAllWithEagerRelationships(pageable);
        } else {
            page = utilityProviderService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utility-providers/:id} : get the "id" utilityProvider.
     *
     * @param id the id of the utilityProviderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilityProviderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtilityProviderDTO> getUtilityProvider(@PathVariable Long id) {
        log.debug("REST request to get UtilityProvider : {}", id);
        Optional<UtilityProviderDTO> utilityProviderDTO = utilityProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utilityProviderDTO);
    }

    /**
     * {@code DELETE  /utility-providers/:id} : delete the "id" utilityProvider.
     *
     * @param id the id of the utilityProviderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilityProvider(@PathVariable Long id) {
        log.debug("REST request to delete UtilityProvider : {}", id);
        utilityProviderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
