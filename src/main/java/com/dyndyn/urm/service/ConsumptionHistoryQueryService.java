package com.dyndyn.urm.service;

import com.dyndyn.urm.domain.*; // for static metamodels
import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.repository.ConsumptionHistoryRepository;
import com.dyndyn.urm.service.criteria.ConsumptionHistoryCriteria;
import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import com.dyndyn.urm.service.mapper.ConsumptionHistoryMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ConsumptionHistory} entities in the database.
 * The main input is a {@link ConsumptionHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConsumptionHistoryDTO} or a {@link Page} of {@link ConsumptionHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsumptionHistoryQueryService extends QueryService<ConsumptionHistory> {

    private final Logger log = LoggerFactory.getLogger(ConsumptionHistoryQueryService.class);

    private final ConsumptionHistoryRepository consumptionHistoryRepository;

    private final ConsumptionHistoryMapper consumptionHistoryMapper;

    public ConsumptionHistoryQueryService(
        ConsumptionHistoryRepository consumptionHistoryRepository,
        ConsumptionHistoryMapper consumptionHistoryMapper
    ) {
        this.consumptionHistoryRepository = consumptionHistoryRepository;
        this.consumptionHistoryMapper = consumptionHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link ConsumptionHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConsumptionHistoryDTO> findByCriteria(ConsumptionHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConsumptionHistory> specification = createSpecification(criteria);
        return consumptionHistoryMapper.toDto(consumptionHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConsumptionHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsumptionHistoryDTO> findByCriteria(ConsumptionHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsumptionHistory> specification = createSpecification(criteria);
        return consumptionHistoryRepository.findAll(specification, page).map(consumptionHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsumptionHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConsumptionHistory> specification = createSpecification(criteria);
        return consumptionHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsumptionHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConsumptionHistory> createSpecification(ConsumptionHistoryCriteria criteria) {
        Specification<ConsumptionHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConsumptionHistory_.id));
            }
            if (criteria.getConsumption() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConsumption(), ConsumptionHistory_.consumption));
            }
            if (criteria.getCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCost(), ConsumptionHistory_.cost));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ConsumptionHistory_.date));
            }
            if (criteria.getHouseholdUtilityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHouseholdUtilityId(),
                            root -> root.join(ConsumptionHistory_.householdUtility, JoinType.LEFT).get(HouseholdUtility_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
