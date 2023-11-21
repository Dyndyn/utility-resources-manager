package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.domain.UtilityProvider;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Utility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilityRepository extends JpaRepository<Utility, Long> {
    Utility findByUtilityProviders(UtilityProvider utilityProvider);
}
