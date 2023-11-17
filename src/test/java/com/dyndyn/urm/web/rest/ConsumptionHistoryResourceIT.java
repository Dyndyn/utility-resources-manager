package com.dyndyn.urm.web.rest;

import static com.dyndyn.urm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dyndyn.urm.IntegrationTest;
import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import com.dyndyn.urm.service.mapper.ConsumptionHistoryMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ConsumptionHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumptionHistoryResourceIT {

    private static final BigDecimal DEFAULT_CONSUMPTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONSUMPTION = new BigDecimal(2);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/consumption-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumptionHistoryRepository consumptionHistoryRepository;

    @Autowired
    private ConsumptionHistoryMapper consumptionHistoryMapper;

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
        ConsumptionHistory consumptionHistory = new ConsumptionHistory().consumption(DEFAULT_CONSUMPTION).date(DEFAULT_DATE);
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
        ConsumptionHistory consumptionHistory = new ConsumptionHistory().consumption(UPDATED_CONSUMPTION).date(UPDATED_DATE);
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
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
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
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
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
        updatedConsumptionHistory.consumption(UPDATED_CONSUMPTION).date(UPDATED_DATE);
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

        partialUpdatedConsumptionHistory.date(UPDATED_DATE);

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
        assertThat(testConsumptionHistory.getConsumption()).isEqualByComparingTo(DEFAULT_CONSUMPTION);
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

        partialUpdatedConsumptionHistory.consumption(UPDATED_CONSUMPTION).date(UPDATED_DATE);

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
