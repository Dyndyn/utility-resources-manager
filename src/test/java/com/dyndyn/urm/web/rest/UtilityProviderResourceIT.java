package com.dyndyn.urm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dyndyn.urm.IntegrationTest;
import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.domain.UtilityProvider;
import com.dyndyn.urm.repository.UtilityProviderRepository;
import com.dyndyn.urm.service.UtilityProviderService;
import com.dyndyn.urm.service.dto.UtilityProviderDTO;
import com.dyndyn.urm.service.mapper.UtilityProviderMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtilityProviderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilityProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_USREOU = "AAAAAAAAAA";
    private static final String UPDATED_USREOU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utility-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilityProviderRepository utilityProviderRepository;

    @Mock
    private UtilityProviderRepository utilityProviderRepositoryMock;

    @Autowired
    private UtilityProviderMapper utilityProviderMapper;

    @Mock
    private UtilityProviderService utilityProviderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilityProviderMockMvc;

    private UtilityProvider utilityProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilityProvider createEntity(EntityManager em) {
        UtilityProvider utilityProvider = new UtilityProvider().name(DEFAULT_NAME).iban(DEFAULT_IBAN).usreou(DEFAULT_USREOU);
        // Add required entity
        Utility utility;
        if (TestUtil.findAll(em, Utility.class).isEmpty()) {
            utility = UtilityResourceIT.createEntity(em);
            em.persist(utility);
            em.flush();
        } else {
            utility = TestUtil.findAll(em, Utility.class).get(0);
        }
        utilityProvider.setUtility(utility);
        return utilityProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilityProvider createUpdatedEntity(EntityManager em) {
        UtilityProvider utilityProvider = new UtilityProvider().name(UPDATED_NAME).iban(UPDATED_IBAN).usreou(UPDATED_USREOU);
        // Add required entity
        Utility utility;
        if (TestUtil.findAll(em, Utility.class).isEmpty()) {
            utility = UtilityResourceIT.createUpdatedEntity(em);
            em.persist(utility);
            em.flush();
        } else {
            utility = TestUtil.findAll(em, Utility.class).get(0);
        }
        utilityProvider.setUtility(utility);
        return utilityProvider;
    }

    @BeforeEach
    public void initTest() {
        utilityProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createUtilityProvider() throws Exception {
        int databaseSizeBeforeCreate = utilityProviderRepository.findAll().size();
        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);
        restUtilityProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeCreate + 1);
        UtilityProvider testUtilityProvider = utilityProviderList.get(utilityProviderList.size() - 1);
        assertThat(testUtilityProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUtilityProvider.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testUtilityProvider.getUsreou()).isEqualTo(DEFAULT_USREOU);
    }

    @Test
    @Transactional
    void createUtilityProviderWithExistingId() throws Exception {
        // Create the UtilityProvider with an existing ID
        utilityProvider.setId(1L);
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        int databaseSizeBeforeCreate = utilityProviderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilityProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilityProviderRepository.findAll().size();
        // set the field null
        utilityProvider.setName(null);

        // Create the UtilityProvider, which fails.
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        restUtilityProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilityProviders() throws Exception {
        // Initialize the database
        utilityProviderRepository.saveAndFlush(utilityProvider);

        // Get all the utilityProviderList
        restUtilityProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilityProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].usreou").value(hasItem(DEFAULT_USREOU)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilityProvidersWithEagerRelationshipsIsEnabled() throws Exception {
        when(utilityProviderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilityProviderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilityProviderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilityProvidersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(utilityProviderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilityProviderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(utilityProviderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUtilityProvider() throws Exception {
        // Initialize the database
        utilityProviderRepository.saveAndFlush(utilityProvider);

        // Get the utilityProvider
        restUtilityProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, utilityProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilityProvider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.usreou").value(DEFAULT_USREOU));
    }

    @Test
    @Transactional
    void getNonExistingUtilityProvider() throws Exception {
        // Get the utilityProvider
        restUtilityProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilityProvider() throws Exception {
        // Initialize the database
        utilityProviderRepository.saveAndFlush(utilityProvider);

        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();

        // Update the utilityProvider
        UtilityProvider updatedUtilityProvider = utilityProviderRepository.findById(utilityProvider.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtilityProvider are not directly saved in db
        em.detach(updatedUtilityProvider);
        updatedUtilityProvider.name(UPDATED_NAME).iban(UPDATED_IBAN).usreou(UPDATED_USREOU);
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(updatedUtilityProvider);

        restUtilityProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilityProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isOk());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
        UtilityProvider testUtilityProvider = utilityProviderList.get(utilityProviderList.size() - 1);
        assertThat(testUtilityProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUtilityProvider.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testUtilityProvider.getUsreou()).isEqualTo(UPDATED_USREOU);
    }

    @Test
    @Transactional
    void putNonExistingUtilityProvider() throws Exception {
        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();
        utilityProvider.setId(longCount.incrementAndGet());

        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilityProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilityProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilityProvider() throws Exception {
        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();
        utilityProvider.setId(longCount.incrementAndGet());

        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilityProvider() throws Exception {
        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();
        utilityProvider.setId(longCount.incrementAndGet());

        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityProviderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilityProviderWithPatch() throws Exception {
        // Initialize the database
        utilityProviderRepository.saveAndFlush(utilityProvider);

        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();

        // Update the utilityProvider using partial update
        UtilityProvider partialUpdatedUtilityProvider = new UtilityProvider();
        partialUpdatedUtilityProvider.setId(utilityProvider.getId());

        partialUpdatedUtilityProvider.name(UPDATED_NAME).iban(UPDATED_IBAN).usreou(UPDATED_USREOU);

        restUtilityProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilityProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilityProvider))
            )
            .andExpect(status().isOk());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
        UtilityProvider testUtilityProvider = utilityProviderList.get(utilityProviderList.size() - 1);
        assertThat(testUtilityProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUtilityProvider.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testUtilityProvider.getUsreou()).isEqualTo(UPDATED_USREOU);
    }

    @Test
    @Transactional
    void fullUpdateUtilityProviderWithPatch() throws Exception {
        // Initialize the database
        utilityProviderRepository.saveAndFlush(utilityProvider);

        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();

        // Update the utilityProvider using partial update
        UtilityProvider partialUpdatedUtilityProvider = new UtilityProvider();
        partialUpdatedUtilityProvider.setId(utilityProvider.getId());

        partialUpdatedUtilityProvider.name(UPDATED_NAME).iban(UPDATED_IBAN).usreou(UPDATED_USREOU);

        restUtilityProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilityProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilityProvider))
            )
            .andExpect(status().isOk());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
        UtilityProvider testUtilityProvider = utilityProviderList.get(utilityProviderList.size() - 1);
        assertThat(testUtilityProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUtilityProvider.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testUtilityProvider.getUsreou()).isEqualTo(UPDATED_USREOU);
    }

    @Test
    @Transactional
    void patchNonExistingUtilityProvider() throws Exception {
        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();
        utilityProvider.setId(longCount.incrementAndGet());

        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilityProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilityProviderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilityProvider() throws Exception {
        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();
        utilityProvider.setId(longCount.incrementAndGet());

        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilityProvider() throws Exception {
        int databaseSizeBeforeUpdate = utilityProviderRepository.findAll().size();
        utilityProvider.setId(longCount.incrementAndGet());

        // Create the UtilityProvider
        UtilityProviderDTO utilityProviderDTO = utilityProviderMapper.toDto(utilityProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilityProviderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilityProviderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilityProvider in the database
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilityProvider() throws Exception {
        // Initialize the database
        utilityProviderRepository.saveAndFlush(utilityProvider);

        int databaseSizeBeforeDelete = utilityProviderRepository.findAll().size();

        // Delete the utilityProvider
        restUtilityProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilityProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UtilityProvider> utilityProviderList = utilityProviderRepository.findAll();
        assertThat(utilityProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
