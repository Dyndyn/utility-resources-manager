package com.dyndyn.urm.web.rest;

import static com.dyndyn.urm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dyndyn.urm.IntegrationTest;
import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.service.ConsumptionHistoryService;
import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import com.dyndyn.urm.service.mapper.ConsumptionHistoryMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ConsumptionHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConsumptionHistoryResourceIT {

    private static final BigDecimal DEFAULT_CONSUMPTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONSUMPTION = new BigDecimal(2);
    private static final BigDecimal SMALLER_CONSUMPTION = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_COST = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/consumption-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumptionHistoryRepository consumptionHistoryRepository;

    @Mock
    private ConsumptionHistoryRepository consumptionHistoryRepositoryMock;

    @Autowired
    private ConsumptionHistoryMapper consumptionHistoryMapper;

    @Mock
    private ConsumptionHistoryService consumptionHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumptionHistoryMockMvc;

    private ConsumptionHistory consumptionHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumptionHistory createEntity(EntityManager em) {
        ConsumptionHistory consumptionHistory = new ConsumptionHistory()
            .consumption(DEFAULT_CONSUMPTION)
            .cost(DEFAULT_COST)
            .date(DEFAULT_DATE);
        // Add required entity
        HouseholdUtility householdUtility;
        if (TestUtil.findAll(em, HouseholdUtility.class).isEmpty()) {
            householdUtility = HouseholdUtilityResourceIT.createEntity(em);
            em.persist(householdUtility);
            em.flush();
        } else {
            householdUtility = TestUtil.findAll(em, HouseholdUtility.class).get(0);
        }
        consumptionHistory.setHouseholdUtility(householdUtility);
        return consumptionHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumptionHistory createUpdatedEntity(EntityManager em) {
        ConsumptionHistory consumptionHistory = new ConsumptionHistory()
            .consumption(UPDATED_CONSUMPTION)
            .cost(UPDATED_COST)
            .date(UPDATED_DATE);
        // Add required entity
        HouseholdUtility householdUtility;
        if (TestUtil.findAll(em, HouseholdUtility.class).isEmpty()) {
            householdUtility = HouseholdUtilityResourceIT.createUpdatedEntity(em);
            em.persist(householdUtility);
            em.flush();
        } else {
            householdUtility = TestUtil.findAll(em, HouseholdUtility.class).get(0);
        }
        consumptionHistory.setHouseholdUtility(householdUtility);
        return consumptionHistory;
    }

    @BeforeEach
    public void initTest() {
        consumptionHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createConsumptionHistory() throws Exception {
        int databaseSizeBeforeCreate = consumptionHistoryRepository.findAll().size();
        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);
        restConsumptionHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ConsumptionHistory testConsumptionHistory = consumptionHistoryList.get(consumptionHistoryList.size() - 1);
        assertThat(testConsumptionHistory.getConsumption()).isEqualByComparingTo(DEFAULT_CONSUMPTION);
        assertThat(testConsumptionHistory.getCost()).isEqualByComparingTo(DEFAULT_COST);
        assertThat(testConsumptionHistory.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createConsumptionHistoryWithExistingId() throws Exception {
        // Create the ConsumptionHistory with an existing ID
        consumptionHistory.setId(1L);
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        int databaseSizeBeforeCreate = consumptionHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumptionHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConsumptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumptionHistoryRepository.findAll().size();
        // set the field null
        consumptionHistory.setConsumption(null);

        // Create the ConsumptionHistory, which fails.
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        restConsumptionHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumptionHistoryRepository.findAll().size();
        // set the field null
        consumptionHistory.setDate(null);

        // Create the ConsumptionHistory, which fails.
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        restConsumptionHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsumptionHistories() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList
        restConsumptionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumptionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].consumption").value(hasItem(sameNumber(DEFAULT_CONSUMPTION))))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsumptionHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(consumptionHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsumptionHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(consumptionHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsumptionHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(consumptionHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsumptionHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(consumptionHistoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConsumptionHistory() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get the consumptionHistory
        restConsumptionHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, consumptionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumptionHistory.getId().intValue()))
            .andExpect(jsonPath("$.consumption").value(sameNumber(DEFAULT_CONSUMPTION)))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getConsumptionHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        Long id = consumptionHistory.getId();

        defaultConsumptionHistoryShouldBeFound("id.equals=" + id);
        defaultConsumptionHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultConsumptionHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConsumptionHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultConsumptionHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConsumptionHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption equals to DEFAULT_CONSUMPTION
        defaultConsumptionHistoryShouldBeFound("consumption.equals=" + DEFAULT_CONSUMPTION);

        // Get all the consumptionHistoryList where consumption equals to UPDATED_CONSUMPTION
        defaultConsumptionHistoryShouldNotBeFound("consumption.equals=" + UPDATED_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsInShouldWork() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption in DEFAULT_CONSUMPTION or UPDATED_CONSUMPTION
        defaultConsumptionHistoryShouldBeFound("consumption.in=" + DEFAULT_CONSUMPTION + "," + UPDATED_CONSUMPTION);

        // Get all the consumptionHistoryList where consumption equals to UPDATED_CONSUMPTION
        defaultConsumptionHistoryShouldNotBeFound("consumption.in=" + UPDATED_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption is not null
        defaultConsumptionHistoryShouldBeFound("consumption.specified=true");

        // Get all the consumptionHistoryList where consumption is null
        defaultConsumptionHistoryShouldNotBeFound("consumption.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption is greater than or equal to DEFAULT_CONSUMPTION
        defaultConsumptionHistoryShouldBeFound("consumption.greaterThanOrEqual=" + DEFAULT_CONSUMPTION);

        // Get all the consumptionHistoryList where consumption is greater than or equal to UPDATED_CONSUMPTION
        defaultConsumptionHistoryShouldNotBeFound("consumption.greaterThanOrEqual=" + UPDATED_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption is less than or equal to DEFAULT_CONSUMPTION
        defaultConsumptionHistoryShouldBeFound("consumption.lessThanOrEqual=" + DEFAULT_CONSUMPTION);

        // Get all the consumptionHistoryList where consumption is less than or equal to SMALLER_CONSUMPTION
        defaultConsumptionHistoryShouldNotBeFound("consumption.lessThanOrEqual=" + SMALLER_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsLessThanSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption is less than DEFAULT_CONSUMPTION
        defaultConsumptionHistoryShouldNotBeFound("consumption.lessThan=" + DEFAULT_CONSUMPTION);

        // Get all the consumptionHistoryList where consumption is less than UPDATED_CONSUMPTION
        defaultConsumptionHistoryShouldBeFound("consumption.lessThan=" + UPDATED_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByConsumptionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where consumption is greater than DEFAULT_CONSUMPTION
        defaultConsumptionHistoryShouldNotBeFound("consumption.greaterThan=" + DEFAULT_CONSUMPTION);

        // Get all the consumptionHistoryList where consumption is greater than SMALLER_CONSUMPTION
        defaultConsumptionHistoryShouldBeFound("consumption.greaterThan=" + SMALLER_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost equals to DEFAULT_COST
        defaultConsumptionHistoryShouldBeFound("cost.equals=" + DEFAULT_COST);

        // Get all the consumptionHistoryList where cost equals to UPDATED_COST
        defaultConsumptionHistoryShouldNotBeFound("cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsInShouldWork() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost in DEFAULT_COST or UPDATED_COST
        defaultConsumptionHistoryShouldBeFound("cost.in=" + DEFAULT_COST + "," + UPDATED_COST);

        // Get all the consumptionHistoryList where cost equals to UPDATED_COST
        defaultConsumptionHistoryShouldNotBeFound("cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost is not null
        defaultConsumptionHistoryShouldBeFound("cost.specified=true");

        // Get all the consumptionHistoryList where cost is null
        defaultConsumptionHistoryShouldNotBeFound("cost.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost is greater than or equal to DEFAULT_COST
        defaultConsumptionHistoryShouldBeFound("cost.greaterThanOrEqual=" + DEFAULT_COST);

        // Get all the consumptionHistoryList where cost is greater than or equal to UPDATED_COST
        defaultConsumptionHistoryShouldNotBeFound("cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost is less than or equal to DEFAULT_COST
        defaultConsumptionHistoryShouldBeFound("cost.lessThanOrEqual=" + DEFAULT_COST);

        // Get all the consumptionHistoryList where cost is less than or equal to SMALLER_COST
        defaultConsumptionHistoryShouldNotBeFound("cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost is less than DEFAULT_COST
        defaultConsumptionHistoryShouldNotBeFound("cost.lessThan=" + DEFAULT_COST);

        // Get all the consumptionHistoryList where cost is less than UPDATED_COST
        defaultConsumptionHistoryShouldBeFound("cost.lessThan=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where cost is greater than DEFAULT_COST
        defaultConsumptionHistoryShouldNotBeFound("cost.greaterThan=" + DEFAULT_COST);

        // Get all the consumptionHistoryList where cost is greater than SMALLER_COST
        defaultConsumptionHistoryShouldBeFound("cost.greaterThan=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date equals to DEFAULT_DATE
        defaultConsumptionHistoryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the consumptionHistoryList where date equals to UPDATED_DATE
        defaultConsumptionHistoryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date in DEFAULT_DATE or UPDATED_DATE
        defaultConsumptionHistoryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the consumptionHistoryList where date equals to UPDATED_DATE
        defaultConsumptionHistoryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date is not null
        defaultConsumptionHistoryShouldBeFound("date.specified=true");

        // Get all the consumptionHistoryList where date is null
        defaultConsumptionHistoryShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date is greater than or equal to DEFAULT_DATE
        defaultConsumptionHistoryShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the consumptionHistoryList where date is greater than or equal to UPDATED_DATE
        defaultConsumptionHistoryShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date is less than or equal to DEFAULT_DATE
        defaultConsumptionHistoryShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the consumptionHistoryList where date is less than or equal to SMALLER_DATE
        defaultConsumptionHistoryShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date is less than DEFAULT_DATE
        defaultConsumptionHistoryShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the consumptionHistoryList where date is less than UPDATED_DATE
        defaultConsumptionHistoryShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        // Get all the consumptionHistoryList where date is greater than DEFAULT_DATE
        defaultConsumptionHistoryShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the consumptionHistoryList where date is greater than SMALLER_DATE
        defaultConsumptionHistoryShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllConsumptionHistoriesByHouseholdUtilityIsEqualToSomething() throws Exception {
        HouseholdUtility householdUtility;
        if (TestUtil.findAll(em, HouseholdUtility.class).isEmpty()) {
            consumptionHistoryRepository.saveAndFlush(consumptionHistory);
            householdUtility = HouseholdUtilityResourceIT.createEntity(em);
        } else {
            householdUtility = TestUtil.findAll(em, HouseholdUtility.class).get(0);
        }
        em.persist(householdUtility);
        em.flush();
        consumptionHistory.setHouseholdUtility(householdUtility);
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);
        Long householdUtilityId = householdUtility.getId();
        // Get all the consumptionHistoryList where householdUtility equals to householdUtilityId
        defaultConsumptionHistoryShouldBeFound("householdUtilityId.equals=" + householdUtilityId);

        // Get all the consumptionHistoryList where householdUtility equals to (householdUtilityId + 1)
        defaultConsumptionHistoryShouldNotBeFound("householdUtilityId.equals=" + (householdUtilityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsumptionHistoryShouldBeFound(String filter) throws Exception {
        restConsumptionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumptionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].consumption").value(hasItem(sameNumber(DEFAULT_CONSUMPTION))))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restConsumptionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsumptionHistoryShouldNotBeFound(String filter) throws Exception {
        restConsumptionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsumptionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsumptionHistory() throws Exception {
        // Get the consumptionHistory
        restConsumptionHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsumptionHistory() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();

        // Update the consumptionHistory
        ConsumptionHistory updatedConsumptionHistory = consumptionHistoryRepository.findById(consumptionHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsumptionHistory are not directly saved in db
        em.detach(updatedConsumptionHistory);
        updatedConsumptionHistory.consumption(UPDATED_CONSUMPTION).cost(UPDATED_COST).date(UPDATED_DATE);
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(updatedConsumptionHistory);

        restConsumptionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumptionHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
        ConsumptionHistory testConsumptionHistory = consumptionHistoryList.get(consumptionHistoryList.size() - 1);
        assertThat(testConsumptionHistory.getConsumption()).isEqualByComparingTo(UPDATED_CONSUMPTION);
        assertThat(testConsumptionHistory.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testConsumptionHistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingConsumptionHistory() throws Exception {
        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();
        consumptionHistory.setId(longCount.incrementAndGet());

        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumptionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumptionHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumptionHistory() throws Exception {
        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();
        consumptionHistory.setId(longCount.incrementAndGet());

        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumptionHistory() throws Exception {
        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();
        consumptionHistory.setId(longCount.incrementAndGet());

        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumptionHistoryWithPatch() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();

        // Update the consumptionHistory using partial update
        ConsumptionHistory partialUpdatedConsumptionHistory = new ConsumptionHistory();
        partialUpdatedConsumptionHistory.setId(consumptionHistory.getId());

        partialUpdatedConsumptionHistory.consumption(UPDATED_CONSUMPTION).cost(UPDATED_COST).date(UPDATED_DATE);

        restConsumptionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumptionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumptionHistory))
            )
            .andExpect(status().isOk());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
        ConsumptionHistory testConsumptionHistory = consumptionHistoryList.get(consumptionHistoryList.size() - 1);
        assertThat(testConsumptionHistory.getConsumption()).isEqualByComparingTo(UPDATED_CONSUMPTION);
        assertThat(testConsumptionHistory.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testConsumptionHistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateConsumptionHistoryWithPatch() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();

        // Update the consumptionHistory using partial update
        ConsumptionHistory partialUpdatedConsumptionHistory = new ConsumptionHistory();
        partialUpdatedConsumptionHistory.setId(consumptionHistory.getId());

        partialUpdatedConsumptionHistory.consumption(UPDATED_CONSUMPTION).cost(UPDATED_COST).date(UPDATED_DATE);

        restConsumptionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumptionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumptionHistory))
            )
            .andExpect(status().isOk());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
        ConsumptionHistory testConsumptionHistory = consumptionHistoryList.get(consumptionHistoryList.size() - 1);
        assertThat(testConsumptionHistory.getConsumption()).isEqualByComparingTo(UPDATED_CONSUMPTION);
        assertThat(testConsumptionHistory.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testConsumptionHistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingConsumptionHistory() throws Exception {
        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();
        consumptionHistory.setId(longCount.incrementAndGet());

        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumptionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumptionHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumptionHistory() throws Exception {
        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();
        consumptionHistory.setId(longCount.incrementAndGet());

        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumptionHistory() throws Exception {
        int databaseSizeBeforeUpdate = consumptionHistoryRepository.findAll().size();
        consumptionHistory.setId(longCount.incrementAndGet());

        // Create the ConsumptionHistory
        ConsumptionHistoryDTO consumptionHistoryDTO = consumptionHistoryMapper.toDto(consumptionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumptionHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumptionHistory in the database
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumptionHistory() throws Exception {
        // Initialize the database
        consumptionHistoryRepository.saveAndFlush(consumptionHistory);

        int databaseSizeBeforeDelete = consumptionHistoryRepository.findAll().size();

        // Delete the consumptionHistory
        restConsumptionHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumptionHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConsumptionHistory> consumptionHistoryList = consumptionHistoryRepository.findAll();
        assertThat(consumptionHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
