package com.dyndyn.urm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dyndyn.urm.IntegrationTest;
import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.repository.UtilityRepository;
import com.dyndyn.urm.service.dto.UtilityDTO;
import com.dyndyn.urm.service.mapper.UtilityMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtilityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONSTANT = false;
    private static final Boolean UPDATED_CONSTANT = true;

    private static final Boolean DEFAULT_PREDICTABLE = false;
    private static final Boolean UPDATED_PREDICTABLE = true;

    private static final String ENTITY_API_URL = "/api/utilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private UtilityMapper utilityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilityMockMvc;

    private Utility utility;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utility createEntity(EntityManager em) {
        Utility utility = new Utility().name(DEFAULT_NAME).constant(DEFAULT_CONSTANT).predictable(DEFAULT_PREDICTABLE);
        return utility;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utility createUpdatedEntity(EntityManager em) {
        Utility utility = new Utility().name(UPDATED_NAME).constant(UPDATED_CONSTANT).predictable(UPDATED_PREDICTABLE);
        return utility;
    }

    @BeforeEach
    public void initTest() {
        utility = createEntity(em);
    }

    @Test
    @Transactional
    void createUtility() throws Exception {
        int databaseSizeBeforeCreate = utilityRepository.findAll().size();
        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);
        restUtilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityDTO)))
            .andExpect(status().isCreated());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeCreate + 1);
        Utility testUtility = utilityList.get(utilityList.size() - 1);
        assertThat(testUtility.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUtility.getConstant()).isEqualTo(DEFAULT_CONSTANT);
        assertThat(testUtility.getPredictable()).isEqualTo(DEFAULT_PREDICTABLE);
    }

    @Test
    @Transactional
    void createUtilityWithExistingId() throws Exception {
        // Create the Utility with an existing ID
        utility.setId(1L);
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        int databaseSizeBeforeCreate = utilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilityRepository.findAll().size();
        // set the field null
        utility.setName(null);

        // Create the Utility, which fails.
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        restUtilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityDTO)))
            .andExpect(status().isBadRequest());

        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilityRepository.findAll().size();
        // set the field null
        utility.setConstant(null);

        // Create the Utility, which fails.
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        restUtilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityDTO)))
            .andExpect(status().isBadRequest());

        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPredictableIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilityRepository.findAll().size();
        // set the field null
        utility.setPredictable(null);

        // Create the Utility, which fails.
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        restUtilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityDTO)))
            .andExpect(status().isBadRequest());

        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilities() throws Exception {
        // Initialize the database
        utilityRepository.saveAndFlush(utility);

        // Get all the utilityList
        restUtilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utility.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].constant").value(hasItem(DEFAULT_CONSTANT.booleanValue())))
            .andExpect(jsonPath("$.[*].predictable").value(hasItem(DEFAULT_PREDICTABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getUtility() throws Exception {
        // Initialize the database
        utilityRepository.saveAndFlush(utility);

        // Get the utility
        restUtilityMockMvc
            .perform(get(ENTITY_API_URL_ID, utility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utility.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.constant").value(DEFAULT_CONSTANT.booleanValue()))
            .andExpect(jsonPath("$.predictable").value(DEFAULT_PREDICTABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUtility() throws Exception {
        // Get the utility
        restUtilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtility() throws Exception {
        // Initialize the database
        utilityRepository.saveAndFlush(utility);

        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();

        // Update the utility
        Utility updatedUtility = utilityRepository.findById(utility.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtility are not directly saved in db
        em.detach(updatedUtility);
        updatedUtility.name(UPDATED_NAME).constant(UPDATED_CONSTANT).predictable(UPDATED_PREDICTABLE);
        UtilityDTO utilityDTO = utilityMapper.toDto(updatedUtility);

        restUtilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
        Utility testUtility = utilityList.get(utilityList.size() - 1);
        assertThat(testUtility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUtility.getConstant()).isEqualTo(UPDATED_CONSTANT);
        assertThat(testUtility.getPredictable()).isEqualTo(UPDATED_PREDICTABLE);
    }

    @Test
    @Transactional
    void putNonExistingUtility() throws Exception {
        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();
        utility.setId(longCount.incrementAndGet());

        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtility() throws Exception {
        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();
        utility.setId(longCount.incrementAndGet());

        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtility() throws Exception {
        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();
        utility.setId(longCount.incrementAndGet());

        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilityWithPatch() throws Exception {
        // Initialize the database
        utilityRepository.saveAndFlush(utility);

        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();

        // Update the utility using partial update
        Utility partialUpdatedUtility = new Utility();
        partialUpdatedUtility.setId(utility.getId());

        partialUpdatedUtility.constant(UPDATED_CONSTANT).predictable(UPDATED_PREDICTABLE);

        restUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtility.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtility))
            )
            .andExpect(status().isOk());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
        Utility testUtility = utilityList.get(utilityList.size() - 1);
        assertThat(testUtility.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUtility.getConstant()).isEqualTo(UPDATED_CONSTANT);
        assertThat(testUtility.getPredictable()).isEqualTo(UPDATED_PREDICTABLE);
    }

    @Test
    @Transactional
    void fullUpdateUtilityWithPatch() throws Exception {
        // Initialize the database
        utilityRepository.saveAndFlush(utility);

        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();

        // Update the utility using partial update
        Utility partialUpdatedUtility = new Utility();
        partialUpdatedUtility.setId(utility.getId());

        partialUpdatedUtility.name(UPDATED_NAME).constant(UPDATED_CONSTANT).predictable(UPDATED_PREDICTABLE);

        restUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtility.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtility))
            )
            .andExpect(status().isOk());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
        Utility testUtility = utilityList.get(utilityList.size() - 1);
        assertThat(testUtility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUtility.getConstant()).isEqualTo(UPDATED_CONSTANT);
        assertThat(testUtility.getPredictable()).isEqualTo(UPDATED_PREDICTABLE);
    }

    @Test
    @Transactional
    void patchNonExistingUtility() throws Exception {
        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();
        utility.setId(longCount.incrementAndGet());

        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtility() throws Exception {
        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();
        utility.setId(longCount.incrementAndGet());

        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtility() throws Exception {
        int databaseSizeBeforeUpdate = utilityRepository.findAll().size();
        utility.setId(longCount.incrementAndGet());

        // Create the Utility
        UtilityDTO utilityDTO = utilityMapper.toDto(utility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(utilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utility in the database
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtility() throws Exception {
        // Initialize the database
        utilityRepository.saveAndFlush(utility);

        int databaseSizeBeforeDelete = utilityRepository.findAll().size();

        // Delete the utility
        restUtilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, utility.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utility> utilityList = utilityRepository.findAll();
        assertThat(utilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
