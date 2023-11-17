package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.ConsumptionHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsumptionHistory entity.
 */
@Repository
public interface ConsumptionHistoryRepository extends JpaRepository<ConsumptionHistory, Long> {
    default Optional<ConsumptionHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ConsumptionHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ConsumptionHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select consumptionHistory from ConsumptionHistory consumptionHistory left join fetch consumptionHistory.householdUtility",
        countQuery = "select count(consumptionHistory) from ConsumptionHistory consumptionHistory"
    )
    Page<ConsumptionHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select consumptionHistory from ConsumptionHistory consumptionHistory left join fetch consumptionHistory.householdUtility")
    List<ConsumptionHistory> findAllWithToOneRelationships();

    @Query(
        "select consumptionHistory from ConsumptionHistory consumptionHistory left join fetch consumptionHistory.householdUtility where consumptionHistory.id =:id"
    )
    Optional<ConsumptionHistory> findOneWithToOneRelationships(@Param("id") Long id);
}
