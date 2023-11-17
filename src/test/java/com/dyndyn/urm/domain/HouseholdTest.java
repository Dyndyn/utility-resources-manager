package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.CityTestSamples.*;
import static com.dyndyn.urm.domain.HouseholdTestSamples.*;
import static com.dyndyn.urm.domain.HouseholdUtilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HouseholdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Household.class);
        Household household1 = getHouseholdSample1();
        Household household2 = new Household();
        assertThat(household1).isNotEqualTo(household2);

        household2.setId(household1.getId());
        assertThat(household1).isEqualTo(household2);

        household2 = getHouseholdSample2();
        assertThat(household1).isNotEqualTo(household2);
    }

    @Test
    void householdUtilityTest() throws Exception {
        Household household = getHouseholdRandomSampleGenerator();
        HouseholdUtility householdUtilityBack = getHouseholdUtilityRandomSampleGenerator();

        household.addHouseholdUtility(householdUtilityBack);
        assertThat(household.getHouseholdUtilities()).containsOnly(householdUtilityBack);
        assertThat(householdUtilityBack.getHousehold()).isEqualTo(household);

        household.removeHouseholdUtility(householdUtilityBack);
        assertThat(household.getHouseholdUtilities()).doesNotContain(householdUtilityBack);
        assertThat(householdUtilityBack.getHousehold()).isNull();

        household.householdUtilities(new HashSet<>(Set.of(householdUtilityBack)));
        assertThat(household.getHouseholdUtilities()).containsOnly(householdUtilityBack);
        assertThat(householdUtilityBack.getHousehold()).isEqualTo(household);

        household.setHouseholdUtilities(new HashSet<>());
        assertThat(household.getHouseholdUtilities()).doesNotContain(householdUtilityBack);
        assertThat(householdUtilityBack.getHousehold()).isNull();
    }

    @Test
    void cityTest() throws Exception {
        Household household = getHouseholdRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        household.setCity(cityBack);
        assertThat(household.getCity()).isEqualTo(cityBack);

        household.city(null);
        assertThat(household.getCity()).isNull();
    }
}
