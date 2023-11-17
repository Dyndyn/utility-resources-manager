package com.dyndyn.urm.service.mapper;

import com.dyndyn.urm.domain.Utility;
import com.dyndyn.urm.service.dto.UtilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utility} and its DTO {@link UtilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilityMapper extends EntityMapper<UtilityDTO, Utility> {}
