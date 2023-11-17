package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.ConsumptionHistory;
import com.dyndyn.urm.domain.HouseholdUtility;
import com.dyndyn.urm.service.dto.ConsumptionHistoryDTO;
import com.dyndyn.urm.service.dto.HouseholdUtilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConsumptionHistory} and its DTO {@link ConsumptionHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsumptionHistoryMapper extends EntityMapper<ConsumptionHistoryDTO, ConsumptionHistory> {
    @Mapping(target = "householdUtility", source = "householdUtility", qualifiedByName = "householdUtilityName")
    ConsumptionHistoryDTO toDto(ConsumptionHistory s);

    @Named("householdUtilityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HouseholdUtilityDTO toDtoHouseholdUtilityName(HouseholdUtility householdUtility);
}
