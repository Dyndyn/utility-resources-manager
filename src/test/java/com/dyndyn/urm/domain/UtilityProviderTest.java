package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.HouseholdUtilityTestSamples.*;
import static com.dyndyn.urm.domain.UtilityProviderTestSamples.*;
import static com.dyndyn.urm.domain.UtilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilityProviderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilityProvider.class);
        UtilityProvider utilityProvider1 = getUtilityProviderSample1();
        UtilityProvider utilityProvider2 = new UtilityProvider();
        assertThat(utilityProvider1).isNotEqualTo(utilityProvider2);

        utilityProvider2.setId(utilityProvider1.getId());
        assertThat(utilityProvider1).isEqualTo(utilityProvider2);

        utilityProvider2 = getUtilityProviderSample2();
        assertThat(utilityProvider1).isNotEqualTo(utilityProvider2);
    }

    @Test
    void householdUtilityTest() throws Exception {
        UtilityProvider utilityProvider = getUtilityProviderRandomSampleGenerator();
        HouseholdUtility householdUtilityBack = getHouseholdUtilityRandomSampleGenerator();

        utilityProvider.addHouseholdUtility(householdUtilityBack);
        assertThat(utilityProvider.getHouseholdUtilities()).containsOnly(householdUtilityBack);
        assertThat(householdUtilityBack.getUtilityProvider()).isEqualTo(utilityProvider);

        utilityProvider.removeHouseholdUtility(householdUtilityBack);
        assertThat(utilityProvider.getHouseholdUtilities()).doesNotContain(householdUtilityBack);
        assertThat(householdUtilityBack.getUtilityProvider()).isNull();

        utilityProvider.householdUtilities(new HashSet<>(Set.of(householdUtilityBack)));
        assertThat(utilityProvider.getHouseholdUtilities()).containsOnly(householdUtilityBack);
        assertThat(householdUtilityBack.getUtilityProvider()).isEqualTo(utilityProvider);

        utilityProvider.setHouseholdUtilities(new HashSet<>());
        assertThat(utilityProvider.getHouseholdUtilities()).doesNotContain(householdUtilityBack);
        assertThat(householdUtilityBack.getUtilityProvider()).isNull();
    }

    @Test
    void utilityTest() throws Exception {
        UtilityProvider utilityProvider = getUtilityProviderRandomSampleGenerator();
        Utility utilityBack = getUtilityRandomSampleGenerator();

        utilityProvider.setUtility(utilityBack);
        assertThat(utilityProvider.getUtility()).isEqualTo(utilityBack);

        utilityProvider.utility(null);
        assertThat(utilityProvider.getUtility()).isNull();
    }
}
