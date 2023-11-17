package com.dyndyn.urm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilityProviderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilityProviderDTO.class);
        UtilityProviderDTO utilityProviderDTO1 = new UtilityProviderDTO();
        utilityProviderDTO1.setId(1L);
        UtilityProviderDTO utilityProviderDTO2 = new UtilityProviderDTO();
        assertThat(utilityProviderDTO1).isNotEqualTo(utilityProviderDTO2);
        utilityProviderDTO2.setId(utilityProviderDTO1.getId());
        assertThat(utilityProviderDTO1).isEqualTo(utilityProviderDTO2);
        utilityProviderDTO2.setId(2L);
        assertThat(utilityProviderDTO1).isNotEqualTo(utilityProviderDTO2);
        utilityProviderDTO1.setId(null);
        assertThat(utilityProviderDTO1).isNotEqualTo(utilityProviderDTO2);
    }
}
