package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.City;
import com.dyndyn.urm.domain.Region;
import com.dyndyn.urm.service.dto.CityDTO;
import com.dyndyn.urm.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionName")
    CityDTO toDto(City s);

    @Named("regionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionDTO toDtoRegionName(Region region);
}
