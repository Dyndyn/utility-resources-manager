package com.dyndyn.urm.domain;

import static com.dyndyn.urm.domain.UtilityProviderTestSamples.*;
import static com.dyndyn.urm.domain.UtilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utility.class);
        Utility utility1 = getUtilitySample1();
        Utility utility2 = new Utility();
        assertThat(utility1).isNotEqualTo(utility2);

        utility2.setId(utility1.getId());
        assertThat(utility1).isEqualTo(utility2);

        utility2 = getUtilitySample2();
        assertThat(utility1).isNotEqualTo(utility2);
    }

    @Test
    void utilityProviderTest() throws Exception {
        Utility utility = getUtilityRandomSampleGenerator();
        UtilityProvider utilityProviderBack = getUtilityProviderRandomSampleGenerator();

        utility.addUtilityProvider(utilityProviderBack);
        assertThat(utility.getUtilityProviders()).containsOnly(utilityProviderBack);
        assertThat(utilityProviderBack.getUtility()).isEqualTo(utility);

        utility.removeUtilityProvider(utilityProviderBack);
        assertThat(utility.getUtilityProviders()).doesNotContain(utilityProviderBack);
        assertThat(utilityProviderBack.getUtility()).isNull();

        utility.utilityProviders(new HashSet<>(Set.of(utilityProviderBack)));
        assertThat(utility.getUtilityProviders()).containsOnly(utilityProviderBack);
        assertThat(utilityProviderBack.getUtility()).isEqualTo(utility);

        utility.setUtilityProviders(new HashSet<>());
        assertThat(utility.getUtilityProviders()).doesNotContain(utilityProviderBack);
        assertThat(utilityProviderBack.getUtility()).isNull();
    }
}
