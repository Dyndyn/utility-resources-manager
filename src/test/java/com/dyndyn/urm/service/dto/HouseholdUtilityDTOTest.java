package com.dyndyn.urm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HouseholdUtilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseholdUtilityDTO.class);
        HouseholdUtilityDTO householdUtilityDTO1 = new HouseholdUtilityDTO();
        householdUtilityDTO1.setId(1L);
        HouseholdUtilityDTO householdUtilityDTO2 = new HouseholdUtilityDTO();
        assertThat(householdUtilityDTO1).isNotEqualTo(householdUtilityDTO2);
        householdUtilityDTO2.setId(householdUtilityDTO1.getId());
        assertThat(householdUtilityDTO1).isEqualTo(householdUtilityDTO2);
        householdUtilityDTO2.setId(2L);
        assertThat(householdUtilityDTO1).isNotEqualTo(householdUtilityDTO2);
        householdUtilityDTO1.setId(null);
        assertThat(householdUtilityDTO1).isNotEqualTo(householdUtilityDTO2);
    }
}
