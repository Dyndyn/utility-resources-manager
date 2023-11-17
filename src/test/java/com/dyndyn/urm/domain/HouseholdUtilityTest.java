package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.ConsumptionHistoryTestSamples.*;
import static com.dyndyn.urm.domain.HouseholdTestSamples.*;
import static com.dyndyn.urm.domain.HouseholdUtilityTestSamples.*;
import static com.dyndyn.urm.domain.UtilityProviderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HouseholdUtilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseholdUtility.class);
        HouseholdUtility householdUtility1 = getHouseholdUtilitySample1();
        HouseholdUtility householdUtility2 = new HouseholdUtility();
        assertThat(householdUtility1).isNotEqualTo(householdUtility2);

        householdUtility2.setId(householdUtility1.getId());
        assertThat(householdUtility1).isEqualTo(householdUtility2);

        householdUtility2 = getHouseholdUtilitySample2();
        assertThat(householdUtility1).isNotEqualTo(householdUtility2);
    }

    @Test
    void consumptionHistoryTest() throws Exception {
        HouseholdUtility householdUtility = getHouseholdUtilityRandomSampleGenerator();
        ConsumptionHistory consumptionHistoryBack = getConsumptionHistoryRandomSampleGenerator();

        householdUtility.addConsumptionHistory(consumptionHistoryBack);
        assertThat(householdUtility.getConsumptionHistories()).containsOnly(consumptionHistoryBack);
        assertThat(consumptionHistoryBack.getHouseholdUtility()).isEqualTo(householdUtility);

        householdUtility.removeConsumptionHistory(consumptionHistoryBack);
        assertThat(householdUtility.getConsumptionHistories()).doesNotContain(consumptionHistoryBack);
        assertThat(consumptionHistoryBack.getHouseholdUtility()).isNull();

        householdUtility.consumptionHistories(new HashSet<>(Set.of(consumptionHistoryBack)));
        assertThat(householdUtility.getConsumptionHistories()).containsOnly(consumptionHistoryBack);
        assertThat(consumptionHistoryBack.getHouseholdUtility()).isEqualTo(householdUtility);

        householdUtility.setConsumptionHistories(new HashSet<>());
        assertThat(householdUtility.getConsumptionHistories()).doesNotContain(consumptionHistoryBack);
        assertThat(consumptionHistoryBack.getHouseholdUtility()).isNull();
    }

    @Test
    void householdTest() throws Exception {
        HouseholdUtility householdUtility = getHouseholdUtilityRandomSampleGenerator();
        Household householdBack = getHouseholdRandomSampleGenerator();

        householdUtility.setHousehold(householdBack);
        assertThat(householdUtility.getHousehold()).isEqualTo(householdBack);

        householdUtility.household(null);
        assertThat(householdUtility.getHousehold()).isNull();
    }

    @Test
    void utilityProviderTest() throws Exception {
        HouseholdUtility householdUtility = getHouseholdUtilityRandomSampleGenerator();
        UtilityProvider utilityProviderBack = getUtilityProviderRandomSampleGenerator();

        householdUtility.setUtilityProvider(utilityProviderBack);
        assertThat(householdUtility.getUtilityProvider()).isEqualTo(utilityProviderBack);

        householdUtility.utilityProvider(null);
        assertThat(householdUtility.getUtilityProvider()).isNull();
    }
}
