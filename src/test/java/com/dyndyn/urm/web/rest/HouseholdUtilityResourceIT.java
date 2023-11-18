package com.dyndyn.urm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dyndyn.urm.IntegrationTest;
import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.Household;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.domain.UtilityProvider;
import com.dyndyn.urm.repository.HouseholdUtilityRepository;
import com.dyndyn.urm.service.HouseholdUtilityService;
import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
import com.dyndyn.urm.service.mapper.HouseholdUtilityMapper;
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
 * Integration tests for the {@link HouseholdUtilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HouseholdUtilityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/household-utilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HouseholdUtilityRepository householdUtilityRepository;

    @Mock
    private HouseholdUtilityRepository householdUtilityRepositoryMock;

    @Autowired
    private HouseholdUtilityMapper householdUtilityMapper;

    @Mock
    private HouseholdUtilityService householdUtilityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHouseholdUtilityMockMvc;

    private HouseholdUtility householdUtility;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HouseholdUtility createEntity(EntityManager em) {
        HouseholdUtility householdUtility = new HouseholdUtility().name(DEFAULT_NAME).accountId(DEFAULT_ACCOUNT_ID).active(DEFAULT_ACTIVE);
        // Add required entity
        Household household;
        if (TestUtil.findAll(em, Household.class).isEmpty()) {
            household = HouseholdResourceIT.createEntity(em);
            em.persist(household);
            em.flush();
        } else {
            household = TestUtil.findAll(em, Household.class).get(0);
        }
        householdUtility.setHousehold(household);
        // Add required entity
        UtilityProvider utilityProvider;
        if (TestUtil.findAll(em, UtilityProvider.class).isEmpty()) {
            utilityProvider = UtilityProviderResourceIT.createEntity(em);
            em.persist(utilityProvider);
            em.flush();
        } else {
            utilityProvider = TestUtil.findAll(em, UtilityProvider.class).get(0);
        }
        householdUtility.setUtilityProvider(utilityProvider);
        return householdUtility;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HouseholdUtility createUpdatedEntity(EntityManager em) {
        HouseholdUtility householdUtility = new HouseholdUtility().name(UPDATED_NAME).accountId(UPDATED_ACCOUNT_ID).active(UPDATED_ACTIVE);
        // Add required entity
        Household household;
        if (TestUtil.findAll(em, Household.class).isEmpty()) {
            household = HouseholdResourceIT.createUpdatedEntity(em);
            em.persist(household);
            em.flush();
        } else {
            household = TestUtil.findAll(em, Household.class).get(0);
        }
        householdUtility.setHousehold(household);
        // Add required entity
        UtilityProvider utilityProvider;
        if (TestUtil.findAll(em, UtilityProvider.class).isEmpty()) {
            utilityProvider = UtilityProviderResourceIT.createUpdatedEntity(em);
            em.persist(utilityProvider);
            em.flush();
        } else {
            utilityProvider = TestUtil.findAll(em, UtilityProvider.class).get(0);
        }
        householdUtility.setUtilityProvider(utilityProvider);
        return householdUtility;
    }

    @BeforeEach
    public void initTest() {
        householdUtility = createEntity(em);
    }

    @Test
    @Transactional
    void createHouseholdUtility() throws Exception {
        int databaseSizeBeforeCreate = householdUtilityRepository.findAll().size();
        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);
        restHouseholdUtilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeCreate + 1);
        HouseholdUtility testHouseholdUtility = householdUtilityList.get(householdUtilityList.size() - 1);
        assertThat(testHouseholdUtility.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHouseholdUtility.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testHouseholdUtility.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createHouseholdUtilityWithExistingId() throws Exception {
        // Create the HouseholdUtility with an existing ID
        householdUtility.setId(1L);
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        int databaseSizeBeforeCreate = householdUtilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseholdUtilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = householdUtilityRepository.findAll().size();
        // set the field null
        householdUtility.setName(null);

        // Create the HouseholdUtility, which fails.
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        restHouseholdUtilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = householdUtilityRepository.findAll().size();
        // set the field null
        householdUtility.setAccountId(null);

        // Create the HouseholdUtility, which fails.
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        restHouseholdUtilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = householdUtilityRepository.findAll().size();
        // set the field null
        householdUtility.setActive(null);

        // Create the HouseholdUtility, which fails.
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        restHouseholdUtilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilities() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList
        restHouseholdUtilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(householdUtility.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHouseholdUtilitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(householdUtilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHouseholdUtilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(householdUtilityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHouseholdUtilitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(householdUtilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHouseholdUtilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(householdUtilityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHouseholdUtility() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get the householdUtility
        restHouseholdUtilityMockMvc
            .perform(get(ENTITY_API_URL_ID, householdUtility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(householdUtility.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getHouseholdUtilitiesByIdFiltering() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        Long id = householdUtility.getId();

        defaultHouseholdUtilityShouldBeFound("id.equals=" + id);
        defaultHouseholdUtilityShouldNotBeFound("id.notEquals=" + id);

        defaultHouseholdUtilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHouseholdUtilityShouldNotBeFound("id.greaterThan=" + id);

        defaultHouseholdUtilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHouseholdUtilityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where name equals to DEFAULT_NAME
        defaultHouseholdUtilityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the householdUtilityList where name equals to UPDATED_NAME
        defaultHouseholdUtilityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHouseholdUtilityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the householdUtilityList where name equals to UPDATED_NAME
        defaultHouseholdUtilityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where name is not null
        defaultHouseholdUtilityShouldBeFound("name.specified=true");

        // Get all the householdUtilityList where name is null
        defaultHouseholdUtilityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where name contains DEFAULT_NAME
        defaultHouseholdUtilityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the householdUtilityList where name contains UPDATED_NAME
        defaultHouseholdUtilityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where name does not contain DEFAULT_NAME
        defaultHouseholdUtilityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the householdUtilityList where name does not contain UPDATED_NAME
        defaultHouseholdUtilityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByAccountIdIsEqualToSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where accountId equals to DEFAULT_ACCOUNT_ID
        defaultHouseholdUtilityShouldBeFound("accountId.equals=" + DEFAULT_ACCOUNT_ID);

        // Get all the householdUtilityList where accountId equals to UPDATED_ACCOUNT_ID
        defaultHouseholdUtilityShouldNotBeFound("accountId.equals=" + UPDATED_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByAccountIdIsInShouldWork() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where accountId in DEFAULT_ACCOUNT_ID or UPDATED_ACCOUNT_ID
        defaultHouseholdUtilityShouldBeFound("accountId.in=" + DEFAULT_ACCOUNT_ID + "," + UPDATED_ACCOUNT_ID);

        // Get all the householdUtilityList where accountId equals to UPDATED_ACCOUNT_ID
        defaultHouseholdUtilityShouldNotBeFound("accountId.in=" + UPDATED_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByAccountIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where accountId is not null
        defaultHouseholdUtilityShouldBeFound("accountId.specified=true");

        // Get all the householdUtilityList where accountId is null
        defaultHouseholdUtilityShouldNotBeFound("accountId.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByAccountIdContainsSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where accountId contains DEFAULT_ACCOUNT_ID
        defaultHouseholdUtilityShouldBeFound("accountId.contains=" + DEFAULT_ACCOUNT_ID);

        // Get all the householdUtilityList where accountId contains UPDATED_ACCOUNT_ID
        defaultHouseholdUtilityShouldNotBeFound("accountId.contains=" + UPDATED_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByAccountIdNotContainsSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where accountId does not contain DEFAULT_ACCOUNT_ID
        defaultHouseholdUtilityShouldNotBeFound("accountId.doesNotContain=" + DEFAULT_ACCOUNT_ID);

        // Get all the householdUtilityList where accountId does not contain UPDATED_ACCOUNT_ID
        defaultHouseholdUtilityShouldBeFound("accountId.doesNotContain=" + UPDATED_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where active equals to DEFAULT_ACTIVE
        defaultHouseholdUtilityShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the householdUtilityList where active equals to UPDATED_ACTIVE
        defaultHouseholdUtilityShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultHouseholdUtilityShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the householdUtilityList where active equals to UPDATED_ACTIVE
        defaultHouseholdUtilityShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        // Get all the householdUtilityList where active is not null
        defaultHouseholdUtilityShouldBeFound("active.specified=true");

        // Get all the householdUtilityList where active is null
        defaultHouseholdUtilityShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByConsumptionHistoryIsEqualToSomething() throws Exception {
        ConsumptionHistory consumptionHistory;
        if (TestUtil.findAll(em, ConsumptionHistory.class).isEmpty()) {
            householdUtilityRepository.saveAndFlush(householdUtility);
            consumptionHistory = ConsumptionHistoryResourceIT.createEntity(em);
        } else {
            consumptionHistory = TestUtil.findAll(em, ConsumptionHistory.class).get(0);
        }
        em.persist(consumptionHistory);
        em.flush();
        householdUtility.addConsumptionHistory(consumptionHistory);
        householdUtilityRepository.saveAndFlush(householdUtility);
        Long consumptionHistoryId = consumptionHistory.getId();
        // Get all the householdUtilityList where consumptionHistory equals to consumptionHistoryId
        defaultHouseholdUtilityShouldBeFound("consumptionHistoryId.equals=" + consumptionHistoryId);

        // Get all the householdUtilityList where consumptionHistory equals to (consumptionHistoryId + 1)
        defaultHouseholdUtilityShouldNotBeFound("consumptionHistoryId.equals=" + (consumptionHistoryId + 1));
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByHouseholdIsEqualToSomething() throws Exception {
        Household household;
        if (TestUtil.findAll(em, Household.class).isEmpty()) {
            householdUtilityRepository.saveAndFlush(householdUtility);
            household = HouseholdResourceIT.createEntity(em);
        } else {
            household = TestUtil.findAll(em, Household.class).get(0);
        }
        em.persist(household);
        em.flush();
        householdUtility.setHousehold(household);
        householdUtilityRepository.saveAndFlush(householdUtility);
        Long householdId = household.getId();
        // Get all the householdUtilityList where household equals to householdId
        defaultHouseholdUtilityShouldBeFound("householdId.equals=" + householdId);

        // Get all the householdUtilityList where household equals to (householdId + 1)
        defaultHouseholdUtilityShouldNotBeFound("householdId.equals=" + (householdId + 1));
    }

    @Test
    @Transactional
    void getAllHouseholdUtilitiesByUtilityProviderIsEqualToSomething() throws Exception {
        UtilityProvider utilityProvider;
        if (TestUtil.findAll(em, UtilityProvider.class).isEmpty()) {
            householdUtilityRepository.saveAndFlush(householdUtility);
            utilityProvider = UtilityProviderResourceIT.createEntity(em);
        } else {
            utilityProvider = TestUtil.findAll(em, UtilityProvider.class).get(0);
        }
        em.persist(utilityProvider);
        em.flush();
        householdUtility.setUtilityProvider(utilityProvider);
        householdUtilityRepository.saveAndFlush(householdUtility);
        Long utilityProviderId = utilityProvider.getId();
        // Get all the householdUtilityList where utilityProvider equals to utilityProviderId
        defaultHouseholdUtilityShouldBeFound("utilityProviderId.equals=" + utilityProviderId);

        // Get all the householdUtilityList where utilityProvider equals to (utilityProviderId + 1)
        defaultHouseholdUtilityShouldNotBeFound("utilityProviderId.equals=" + (utilityProviderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHouseholdUtilityShouldBeFound(String filter) throws Exception {
        restHouseholdUtilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(householdUtility.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restHouseholdUtilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHouseholdUtilityShouldNotBeFound(String filter) throws Exception {
        restHouseholdUtilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHouseholdUtilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHouseholdUtility() throws Exception {
        // Get the householdUtility
        restHouseholdUtilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHouseholdUtility() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();

        // Update the householdUtility
        HouseholdUtility updatedHouseholdUtility = householdUtilityRepository.findById(householdUtility.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHouseholdUtility are not directly saved in db
        em.detach(updatedHouseholdUtility);
        updatedHouseholdUtility.name(UPDATED_NAME).accountId(UPDATED_ACCOUNT_ID).active(UPDATED_ACTIVE);
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(updatedHouseholdUtility);

        restHouseholdUtilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, householdUtilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
        HouseholdUtility testHouseholdUtility = householdUtilityList.get(householdUtilityList.size() - 1);
        assertThat(testHouseholdUtility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHouseholdUtility.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testHouseholdUtility.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingHouseholdUtility() throws Exception {
        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();
        householdUtility.setId(longCount.incrementAndGet());

        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseholdUtilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, householdUtilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHouseholdUtility() throws Exception {
        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();
        householdUtility.setId(longCount.incrementAndGet());

        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdUtilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHouseholdUtility() throws Exception {
        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();
        householdUtility.setId(longCount.incrementAndGet());

        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdUtilityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHouseholdUtilityWithPatch() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();

        // Update the householdUtility using partial update
        HouseholdUtility partialUpdatedHouseholdUtility = new HouseholdUtility();
        partialUpdatedHouseholdUtility.setId(householdUtility.getId());

        partialUpdatedHouseholdUtility.name(UPDATED_NAME);

        restHouseholdUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouseholdUtility.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouseholdUtility))
            )
            .andExpect(status().isOk());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
        HouseholdUtility testHouseholdUtility = householdUtilityList.get(householdUtilityList.size() - 1);
        assertThat(testHouseholdUtility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHouseholdUtility.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testHouseholdUtility.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateHouseholdUtilityWithPatch() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();

        // Update the householdUtility using partial update
        HouseholdUtility partialUpdatedHouseholdUtility = new HouseholdUtility();
        partialUpdatedHouseholdUtility.setId(householdUtility.getId());

        partialUpdatedHouseholdUtility.name(UPDATED_NAME).accountId(UPDATED_ACCOUNT_ID).active(UPDATED_ACTIVE);

        restHouseholdUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouseholdUtility.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouseholdUtility))
            )
            .andExpect(status().isOk());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
        HouseholdUtility testHouseholdUtility = householdUtilityList.get(householdUtilityList.size() - 1);
        assertThat(testHouseholdUtility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHouseholdUtility.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testHouseholdUtility.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingHouseholdUtility() throws Exception {
        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();
        householdUtility.setId(longCount.incrementAndGet());

        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseholdUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, householdUtilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHouseholdUtility() throws Exception {
        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();
        householdUtility.setId(longCount.incrementAndGet());

        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHouseholdUtility() throws Exception {
        int databaseSizeBeforeUpdate = householdUtilityRepository.findAll().size();
        householdUtility.setId(longCount.incrementAndGet());

        // Create the HouseholdUtility
        HouseholdUtilityDTO householdUtilityDTO = householdUtilityMapper.toDto(householdUtility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseholdUtilityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(householdUtilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HouseholdUtility in the database
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHouseholdUtility() throws Exception {
        // Initialize the database
        householdUtilityRepository.saveAndFlush(householdUtility);

        int databaseSizeBeforeDelete = householdUtilityRepository.findAll().size();

        // Delete the householdUtility
        restHouseholdUtilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, householdUtility.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HouseholdUtility> householdUtilityList = householdUtilityRepository.findAll();
        assertThat(householdUtilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
