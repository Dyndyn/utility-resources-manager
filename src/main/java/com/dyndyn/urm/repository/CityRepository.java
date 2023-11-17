package com.dyndyn.urm.repository;

import com.dyndyn.urm.domain.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the City entity.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    default Optional<City> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<City> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<City> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select city from City city left join fetch city.region", countQuery = "select count(city) from City city")
    Page<City> findAllWithToOneRelationships(Pageable pageable);

    @Query("select city from City city left join fetch city.region")
    List<City> findAllWithToOneRelationships();

    @Query("select city from City city left join fetch city.region where city.id =:id")
    Optional<City> findOneWithToOneRelationships(@Param("id") Long id);
}
