package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.Country;
import com.dyndyn.urm.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
