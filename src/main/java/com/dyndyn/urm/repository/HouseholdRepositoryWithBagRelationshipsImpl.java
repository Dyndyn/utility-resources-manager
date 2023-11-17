package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.Household;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class HouseholdRepositoryWithBagRelationshipsImpl implements HouseholdRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Household> fetchBagRelationships(Optional<Household> household) {
        return household.map(this::fetchUsers);
    }

    @Override
    public Page<Household> fetchBagRelationships(Page<Household> households) {
        return new PageImpl<>(fetchBagRelationships(households.getContent()), households.getPageable(), households.getTotalElements());
    }

    @Override
    public List<Household> fetchBagRelationships(List<Household> households) {
        return Optional.of(households).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Household fetchUsers(Household result) {
        return entityManager
            .createQuery(
                "select household from Household household left join fetch household.users where household.id = :id",
                Household.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Household> fetchUsers(List<Household> households) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, households.size()).forEach(index -> order.put(households.get(index).getId(), index));
        List<Household> result = entityManager
            .createQuery(
                "select household from Household household left join fetch household.users where household in :households",
                Household.class
            )
            .setParameter("households", households)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
