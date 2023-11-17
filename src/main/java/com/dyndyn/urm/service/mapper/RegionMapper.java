package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.Country;
import com.dyndyn.urm.domain.Region;
import com.dyndyn.urm.service.dto.CountryDTO;
import com.dyndyn.urm.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    RegionDTO toDto(Region s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
