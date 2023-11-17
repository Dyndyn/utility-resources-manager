package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.ConsumptionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsumptionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumptionHistoryRepository extends JpaRepository<ConsumptionHistory, Long> {}
