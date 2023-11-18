package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.HouseholdUtility;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HouseholdUtility entity.
 */
@Repository
public interface HouseholdUtilityRepository extends JpaRepository<HouseholdUtility, Long>, JpaSpecificationExecutor<HouseholdUtility> {
    default Optional<HouseholdUtility> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HouseholdUtility> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HouseholdUtility> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select householdUtility from HouseholdUtility householdUtility left join fetch householdUtility.household left join fetch householdUtility.utilityProvider",
        countQuery = "select count(householdUtility) from HouseholdUtility householdUtility"
    )
    Page<HouseholdUtility> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select householdUtility from HouseholdUtility householdUtility left join fetch householdUtility.household left join fetch householdUtility.utilityProvider"
    )
    List<HouseholdUtility> findAllWithToOneRelationships();

    @Query(
        "select householdUtility from HouseholdUtility householdUtility left join fetch householdUtility.household left join fetch householdUtility.utilityProvider where householdUtility.id =:id"
    )
    Optional<HouseholdUtility> findOneWithToOneRelationships(@Param("id") Long id);
}
