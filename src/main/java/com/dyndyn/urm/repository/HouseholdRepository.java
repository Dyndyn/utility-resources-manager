package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.Household;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Household entity.
 *
 * When extending this class, extend HouseholdRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface HouseholdRepository extends HouseholdRepositoryWithBagRelationships, JpaRepository<Household, Long> {
    default Optional<Household> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Household> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Household> findAllWithEagerRelationships(Pageable pageable, String login) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable, login));
    }

    @Query(
        value = "select household from Household household left join fetch household.city join household.users u where u.login = :login",
        countQuery = "select count(household) from Household household join household.users u where u.login = :login"
    )
    Page<Household> findAllWithToOneRelationships(Pageable pageable, @Param("login") String login);

    @Query("select household from Household household left join fetch household.city")
    List<Household> findAllWithToOneRelationships();

    @Query("select household from Household household left join fetch household.city where household.id =:id")
    Optional<Household> findOneWithToOneRelationships(@Param("id") Long id);
}
