package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.CityTestSamples.*;
import static com.dyndyn.urm.domain.HouseholdTestSamples.*;
import static com.dyndyn.urm.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    void householdTest() throws Exception {
        City city = getCityRandomSampleGenerator();
        Household householdBack = getHouseholdRandomSampleGenerator();

        city.addHousehold(householdBack);
        assertThat(city.getHouseholds()).containsOnly(householdBack);
        assertThat(householdBack.getCity()).isEqualTo(city);

        city.removeHousehold(householdBack);
        assertThat(city.getHouseholds()).doesNotContain(householdBack);
        assertThat(householdBack.getCity()).isNull();

        city.households(new HashSet<>(Set.of(householdBack)));
        assertThat(city.getHouseholds()).containsOnly(householdBack);
        assertThat(householdBack.getCity()).isEqualTo(city);

        city.setHouseholds(new HashSet<>());
        assertThat(city.getHouseholds()).doesNotContain(householdBack);
        assertThat(householdBack.getCity()).isNull();
    }

    @Test
    void regionTest() throws Exception {
        City city = getCityRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        city.setRegion(regionBack);
        assertThat(city.getRegion()).isEqualTo(regionBack);

        city.region(null);
        assertThat(city.getRegion()).isNull();
    }
}
