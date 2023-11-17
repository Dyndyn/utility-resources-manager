package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.Household;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HouseholdRepositoryWithBagRelationships {
    Optional<Household> fetchBagRelationships(Optional<Household> household);

    List<Household> fetchBagRelationships(List<Household> households);

    Page<Household> fetchBagRelationships(Page<Household> households);
}
