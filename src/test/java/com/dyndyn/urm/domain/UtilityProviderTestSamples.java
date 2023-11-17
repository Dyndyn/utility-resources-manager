package com.dyndyn.urm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilityProviderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UtilityProvider getUtilityProviderSample1() {
        return new UtilityProvider().id(1L).name("name1").iban("iban1").usreou("usreou1");
    }

    public static UtilityProvider getUtilityProviderSample2() {
        return new UtilityProvider().id(2L).name("name2").iban("iban2").usreou("usreou2");
    }

    public static UtilityProvider getUtilityProviderRandomSampleGenerator() {
        return new UtilityProvider()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString())
            .usreou(UUID.randomUUID().toString());
    }
}
