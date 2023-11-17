package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.ConsumptionHistoryTestSamples.*;
import static com.dyndyn.urm.domain.HouseholdUtilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumptionHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumptionHistory.class);
        ConsumptionHistory consumptionHistory1 = getConsumptionHistorySample1();
        ConsumptionHistory consumptionHistory2 = new ConsumptionHistory();
        assertThat(consumptionHistory1).isNotEqualTo(consumptionHistory2);

        consumptionHistory2.setId(consumptionHistory1.getId());
        assertThat(consumptionHistory1).isEqualTo(consumptionHistory2);

        consumptionHistory2 = getConsumptionHistorySample2();
        assertThat(consumptionHistory1).isNotEqualTo(consumptionHistory2);
    }

    @Test
    void householdUtilityTest() throws Exception {
        ConsumptionHistory consumptionHistory = getConsumptionHistoryRandomSampleGenerator();
        HouseholdUtility householdUtilityBack = getHouseholdUtilityRandomSampleGenerator();

        consumptionHistory.setHouseholdUtility(householdUtilityBack);
        assertThat(consumptionHistory.getHouseholdUtility()).isEqualTo(householdUtilityBack);

        consumptionHistory.householdUtility(null);
        assertThat(consumptionHistory.getHouseholdUtility()).isNull();
    }
}
