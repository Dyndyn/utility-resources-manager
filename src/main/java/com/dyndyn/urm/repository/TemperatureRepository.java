package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.City;
import com.dyndyn.urm.domain.Temperature;
import com.dyndyn.urm.domain.Temperature_;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the City entity.
 */
@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
    Optional<Temperature> findByCityAndDate(City city, LocalDate date);
}
