package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.RowDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public interface ConsumptionHistoryRepository
    extends JpaRepository<ConsumptionHistory, Long>, JpaSpecificationExecutor<ConsumptionHistory> {
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

    @Query(
        value = "SELECT SUM(consumption) / (TIMESTAMPDIFF(MONTH, MIN(date), MAX(date)) + 1) FROM consumption_history WHERE household_utility_id = :id",
        nativeQuery = true
    )
    BigDecimal findMeanConsumptionByHouseholdUtilityId(@Param("id") Long householdUtilityId);

    @Query(
        value = "select new com.dyndyn.urm.domain.RowDTO(h.id, ch.consumption, h.area, h.residents, ch.date, t.temperature) " +
        "from ConsumptionHistory ch " +
        "left join ch.householdUtility hu " +
        "left join hu.household h " +
        "left join h.city c " +
        "left join c.temperatures t on t.date = ch.date " +
        "left join hu.utilityProvider up " +
        "left join up.utility u " +
        " where u.id =:id"
    )
    List<RowDTO> findRowByUtilityId(@Param("id") Long utilityId);

    @Query(
        "select consumptionHistory.consumption from ConsumptionHistory consumptionHistory left join consumptionHistory.householdUtility h where h.id =:id and consumptionHistory.date = :date"
    )
    BigDecimal findConsumptionByHouseholdUtilityIdAndDate(@Param("id") Long householdUtilityId, @Param("date") LocalDate date);
}
