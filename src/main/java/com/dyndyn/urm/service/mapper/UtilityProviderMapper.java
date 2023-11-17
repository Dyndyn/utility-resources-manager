package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.domain.UtilityProvider;
import com.dyndyn.urm.service.dto.UtilityDTO;
import com.dyndyn.urm.service.dto.UtilityProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UtilityProvider} and its DTO {@link UtilityProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilityProviderMapper extends EntityMapper<UtilityProviderDTO, UtilityProvider> {
    @Mapping(target = "utility", source = "utility", qualifiedByName = "utilityName")
    UtilityProviderDTO toDto(UtilityProvider s);

    @Named("utilityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UtilityDTO toDtoUtilityName(Utility utility);
}
