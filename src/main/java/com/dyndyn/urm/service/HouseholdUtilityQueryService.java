package com.dyndyn.urm.service;

import com.dyndyn.urm.domain.*; // for static metamodels
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.repository.HouseholdUtilityRepository;
import com.dyndyn.urm.service.criteria.HouseholdUtilityCriteria;
import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
import com.dyndyn.urm.service.mapper.HouseholdUtilityMapper;
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
 * Service for executing complex queries for {@link HouseholdUtility} entities in the database.
 * The main input is a {@link HouseholdUtilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HouseholdUtilityDTO} or a {@link Page} of {@link HouseholdUtilityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HouseholdUtilityQueryService extends QueryService<HouseholdUtility> {

    private final Logger log = LoggerFactory.getLogger(HouseholdUtilityQueryService.class);

    private final HouseholdUtilityRepository householdUtilityRepository;

    private final HouseholdUtilityMapper householdUtilityMapper;

    public HouseholdUtilityQueryService(
        HouseholdUtilityRepository householdUtilityRepository,
        HouseholdUtilityMapper householdUtilityMapper
    ) {
        this.householdUtilityRepository = householdUtilityRepository;
        this.householdUtilityMapper = householdUtilityMapper;
    }

    /**
     * Return a {@link List} of {@link HouseholdUtilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HouseholdUtilityDTO> findByCriteria(HouseholdUtilityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HouseholdUtility> specification = createSpecification(criteria);
        return householdUtilityMapper.toDto(householdUtilityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HouseholdUtilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseholdUtilityDTO> findByCriteria(HouseholdUtilityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HouseholdUtility> specification = createSpecification(criteria);
        return householdUtilityRepository.findAll(specification, page).map(householdUtilityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HouseholdUtilityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HouseholdUtility> specification = createSpecification(criteria);
        return householdUtilityRepository.count(specification);
    }

    /**
     * Function to convert {@link HouseholdUtilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HouseholdUtility> createSpecification(HouseholdUtilityCriteria criteria) {
        Specification<HouseholdUtility> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HouseholdUtility_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HouseholdUtility_.name));
            }
            if (criteria.getAccountId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountId(), HouseholdUtility_.accountId));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), HouseholdUtility_.rate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), HouseholdUtility_.active));
            }
            if (criteria.getConsumptionHistoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsumptionHistoryId(),
                            root -> root.join(HouseholdUtility_.consumptionHistories, JoinType.LEFT).get(ConsumptionHistory_.id)
                        )
                    );
            }
            if (criteria.getHouseholdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHouseholdId(),
                            root -> root.join(HouseholdUtility_.household, JoinType.LEFT).get(Household_.id)
                        )
                    );
            }
            if (criteria.getUtilityProviderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUtilityProviderId(),
                            root -> root.join(HouseholdUtility_.utilityProvider, JoinType.LEFT).get(UtilityProvider_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
