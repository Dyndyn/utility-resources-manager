package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.CountryTestSamples.*;
import static com.dyndyn.urm.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void regionTest() throws Exception {
        Country country = getCountryRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        country.addRegion(regionBack);
        assertThat(country.getRegions()).containsOnly(regionBack);
        assertThat(regionBack.getCountry()).isEqualTo(country);

        country.removeRegion(regionBack);
        assertThat(country.getRegions()).doesNotContain(regionBack);
        assertThat(regionBack.getCountry()).isNull();

        country.regions(new HashSet<>(Set.of(regionBack)));
        assertThat(country.getRegions()).containsOnly(regionBack);
        assertThat(regionBack.getCountry()).isEqualTo(country);

        country.setRegions(new HashSet<>());
        assertThat(country.getRegions()).doesNotContain(regionBack);
        assertThat(regionBack.getCountry()).isNull();
    }
}
