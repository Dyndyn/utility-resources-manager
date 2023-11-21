package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.ConsumptionPrediction;
import com.dyndyn.urm.domain.ConsumptionPrediction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsumptionPrediction entity.
 */
@Repository
public interface ConsumptionPredictionRepository
    extends JpaRepository<ConsumptionPrediction, Long>, JpaSpecificationExecutor<ConsumptionPrediction> {
    default Optional<ConsumptionPrediction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ConsumptionPrediction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ConsumptionPrediction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select consumptionPrediction from ConsumptionPrediction consumptionPrediction left join fetch consumptionPrediction.householdUtility",
        countQuery = "select count(consumptionPrediction) from ConsumptionPrediction consumptionPrediction"
    )
    Page<ConsumptionPrediction> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select consumptionPrediction from ConsumptionPrediction consumptionPrediction left join fetch consumptionPrediction.householdUtility"
    )
    List<ConsumptionPrediction> findAllWithToOneRelationships();

    @Query(
        "select consumptionPrediction from ConsumptionPrediction consumptionPrediction left join fetch consumptionPrediction.householdUtility where consumptionPrediction.id =:id"
    )
    Optional<ConsumptionPrediction> findOneWithToOneRelationships(@Param("id") Long id);
}
