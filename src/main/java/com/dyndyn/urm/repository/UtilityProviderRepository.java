package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.UtilityProvider;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UtilityProvider entity.
 */
@Repository
public interface UtilityProviderRepository extends JpaRepository<UtilityProvider, Long> {
    default Optional<UtilityProvider> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UtilityProvider> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UtilityProvider> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select utilityProvider from UtilityProvider utilityProvider left join fetch utilityProvider.utility",
        countQuery = "select count(utilityProvider) from UtilityProvider utilityProvider"
    )
    Page<UtilityProvider> findAllWithToOneRelationships(Pageable pageable);

    @Query("select utilityProvider from UtilityProvider utilityProvider left join fetch utilityProvider.utility")
    List<UtilityProvider> findAllWithToOneRelationships();

    @Query(
        "select utilityProvider from UtilityProvider utilityProvider left join fetch utilityProvider.utility where utilityProvider.id =:id"
    )
    Optional<UtilityProvider> findOneWithToOneRelationships(@Param("id") Long id);
}
