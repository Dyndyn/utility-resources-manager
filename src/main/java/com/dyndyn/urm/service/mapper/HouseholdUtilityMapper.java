package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.Household;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.domain.UtilityProvider;
import com.dyndyn.urm.service.dto.HouseholdDTO;
import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
import com.dyndyn.urm.service.dto.UtilityProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HouseholdUtility} and its DTO {@link HouseholdUtilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface HouseholdUtilityMapper extends EntityMapper<HouseholdUtilityDTO, HouseholdUtility> {
    @Mapping(target = "household", source = "household", qualifiedByName = "householdAddress")
    @Mapping(target = "utilityProvider", source = "utilityProvider", qualifiedByName = "utilityProviderName")
    HouseholdUtilityDTO toDto(HouseholdUtility s);

    @Named("householdAddress")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    HouseholdDTO toDtoHouseholdAddress(Household household);

    @Named("utilityProviderName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UtilityProviderDTO toDtoUtilityProviderName(UtilityProvider utilityProvider);
}
