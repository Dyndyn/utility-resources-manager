package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.City;
import com.dyndyn.urm.domain.Household;
import com.dyndyn.urm.domain.User;
import com.dyndyn.urm.service.dto.CityDTO;
import com.dyndyn.urm.service.dto.HouseholdDTO;
import com.dyndyn.urm.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Household} and its DTO {@link HouseholdDTO}.
 */
@Mapper(componentModel = "spring")
public interface HouseholdMapper extends EntityMapper<HouseholdDTO, Household> {
    @Mapping(target = "users", source = "users", qualifiedByName = "userIdSet")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    HouseholdDTO toDto(Household s);

    @Mapping(target = "removeUser", ignore = true)
    Household toEntity(HouseholdDTO householdDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);
}
