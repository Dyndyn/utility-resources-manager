package com.dyndyn.urm.web.rest;

import static com.dyndyn.urm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dyndyn.urm.IntegrationTest;
import com.dyndyn.urm.domain.City;
import com.dyndyn.urm.domain.Household;
import com.dyndyn.urm.repository.HouseholdRepository;
import com.dyndyn.urm.service.HouseholdService;
import com.dyndyn.urm.service.dto.HouseholdDTO;
import com.dyndyn.urm.service.mapper.HouseholdMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link HouseholdResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HouseholdResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AREA = new BigDecimal(2);

    private static final Integer DEFAULT_RESIDENTS = 1;
    private static final Integer UPDATED_RESIDENTS = 2;

    private static final String ENTITY_API_URL = "/api/households";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HouseholdRepository householdRepository;

    @Mock
    private HouseholdRepository householdRepositoryMock;

    @Autowired
    private HouseholdMapper householdMapper;

    @Mock
    private HouseholdService householdServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHouseholdMockMvc;

    private Household household;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Household createEntity(EntityManager em) {
        Household household = new Household().address(DEFAULT_ADDRESS).area(DEFAULT_AREA).residents(DEFAULT_RESIDENTS);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity(em);
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        household.setCity(city);
        return household;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Household createUpdatedEntity(EntityManager em) {
        Household household = new Household().address(UPDATED_ADDRESS).area(UPDATED_AREA).residents(UPDATED_RESIDENTS);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity(em);
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        household.setCity(city);
        return household;
    }

    @BeforeEach
    public void initTest() {
        household = createEntity(em);
    }

    @Test
    @Transactional
    void createHousehold() throws Exception {
        int databaseSizeBeforeCreate = householdRepository.findAll().size();
        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);
        restHouseholdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdDTO)))
            .andExpect(status().isCreated());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeCreate + 1);
        Household testHousehold = householdList.get(householdList.size() - 1);
        assertThat(testHousehold.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHousehold.getArea()).isEqualByComparingTo(DEFAULT_AREA);
        assertThat(testHousehold.getResidents()).isEqualTo(DEFAULT_RESIDENTS);
    }

    @Test
    @Transactional
    void createHouseholdWithExistingId() throws Exception {
        // Create the Household with an existing ID
        household.setId(1L);
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        int databaseSizeBeforeCreate = householdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseholdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = householdRepository.findAll().size();
        // set the field null
        household.setAddress(null);

        // Create the Household, which fails.
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        restHouseholdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdDTO)))
            .andExpect(status().isBadRequest());

        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = householdRepository.findAll().size();
        // set the field null
        household.setArea(null);

        // Create the Household, which fails.
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        restHouseholdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdDTO)))
            .andExpect(status().isBadRequest());

        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = householdRepository.findAll().size();
        // set the field null
        household.setResidents(null);

        // Create the Household, which fails.
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        restHouseholdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdDTO)))
            .andExpect(status().isBadRequest());

        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHouseholds() throws Exception {
        // Initialize the database
        householdRepository.saveAndFlush(household);

        // Get all the householdList
        restHouseholdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(household.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(sameNumber(DEFAULT_AREA))))
            .andExpect(jsonPath("$.[*].residents").value(hasItem(DEFAULT_RESIDENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHouseholdsWithEagerRelationshipsIsEnabled() throws Exception {
        when(householdServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHouseholdMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(householdServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHouseholdsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(householdServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHouseholdMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(householdRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHousehold() throws Exception {
        // Initialize the database
        householdRepository.saveAndFlush(household);

        // Get the household
        restHouseholdMockMvc
            .perform(get(ENTITY_API_URL_ID, household.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(household.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.area").value(sameNumber(DEFAULT_AREA)))
            .andExpect(jsonPath("$.residents").value(DEFAULT_RESIDENTS));
    }

    @Test
    @Transactional
    void getNonExistingHousehold() throws Exception {
        // Get the household
        restHouseholdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHousehold() throws Exception {
        // Initialize the database
        householdRepository.saveAndFlush(household);

        int databaseSizeBeforeUpdate = householdRepository.findAll().size();

        // Update the household
        Household updatedHousehold = householdRepository.findById(household.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHousehold are not directly saved in db
        em.detach(updatedHousehold);
        updatedHousehold.address(UPDATED_ADDRESS).area(UPDATED_AREA).residents(UPDATED_RESIDENTS);
        HouseholdDTO householdDTO = householdMapper.toDto(updatedHousehold);

        restHouseholdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, householdDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(householdDTO))
            )
            .andExpect(status().isOk());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
        Household testHousehold = householdList.get(householdList.size() - 1);
        assertThat(testHousehold.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHousehold.getArea()).isEqualByComparingTo(UPDATED_AREA);
        assertThat(testHousehold.getResidents()).isEqualTo(UPDATED_RESIDENTS);
    }

    @Test
    @Transactional
    void putNonExistingHousehold() throws Exception {
        int databaseSizeBeforeUpdate = householdRepository.findAll().size();
        household.setId(longCount.incrementAndGet());

        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseholdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, householdDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(householdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHousehold() throws Exception {
        int databaseSizeBeforeUpdate = householdRepository.findAll().size();
        household.setId(longCount.incrementAndGet());

        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(householdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHousehold() throws Exception {
        int databaseSizeBeforeUpdate = householdRepository.findAll().size();
        household.setId(longCount.incrementAndGet());

        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHouseholdWithPatch() throws Exception {
        // Initialize the database
        householdRepository.saveAndFlush(household);

        int databaseSizeBeforeUpdate = householdRepository.findAll().size();

        // Update the household using partial update
        Household partialUpdatedHousehold = new Household();
        partialUpdatedHousehold.setId(household.getId());

        partialUpdatedHousehold.area(UPDATED_AREA).residents(UPDATED_RESIDENTS);

        restHouseholdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHousehold.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHousehold))
            )
            .andExpect(status().isOk());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
        Household testHousehold = householdList.get(householdList.size() - 1);
        assertThat(testHousehold.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHousehold.getArea()).isEqualByComparingTo(UPDATED_AREA);
        assertThat(testHousehold.getResidents()).isEqualTo(UPDATED_RESIDENTS);
    }

    @Test
    @Transactional
    void fullUpdateHouseholdWithPatch() throws Exception {
        // Initialize the database
        householdRepository.saveAndFlush(household);

        int databaseSizeBeforeUpdate = householdRepository.findAll().size();

        // Update the household using partial update
        Household partialUpdatedHousehold = new Household();
        partialUpdatedHousehold.setId(household.getId());

        partialUpdatedHousehold.address(UPDATED_ADDRESS).area(UPDATED_AREA).residents(UPDATED_RESIDENTS);

        restHouseholdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHousehold.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHousehold))
            )
            .andExpect(status().isOk());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
        Household testHousehold = householdList.get(householdList.size() - 1);
        assertThat(testHousehold.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHousehold.getArea()).isEqualByComparingTo(UPDATED_AREA);
        assertThat(testHousehold.getResidents()).isEqualTo(UPDATED_RESIDENTS);
    }

    @Test
    @Transactional
    void patchNonExistingHousehold() throws Exception {
        int databaseSizeBeforeUpdate = householdRepository.findAll().size();
        household.setId(longCount.incrementAndGet());

        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseholdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, householdDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(householdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHousehold() throws Exception {
        int databaseSizeBeforeUpdate = householdRepository.findAll().size();
        household.setId(longCount.incrementAndGet());

        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(householdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHousehold() throws Exception {
        int databaseSizeBeforeUpdate = householdRepository.findAll().size();
        household.setId(longCount.incrementAndGet());

        // Create the Household
        HouseholdDTO householdDTO = householdMapper.toDto(household);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(householdDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Household in the database
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHousehold() throws Exception {
        // Initialize the database
        householdRepository.saveAndFlush(household);

        int databaseSizeBeforeDelete = householdRepository.findAll().size();

        // Delete the household
        restHouseholdMockMvc
            .perform(delete(ENTITY_API_URL_ID, household.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Household> householdList = householdRepository.findAll();
        assertThat(householdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
