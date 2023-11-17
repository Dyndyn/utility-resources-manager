package com.dyndyn.urm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HouseholdUtilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HouseholdUtility getHouseholdUtilitySample1() {
        return new HouseholdUtility().id(1L).name("name1").accountId("accountId1");
    }

    public static HouseholdUtility getHouseholdUtilitySample2() {
        return new HouseholdUtility().id(2L).name("name2").accountId("accountId2");
    }

    public static HouseholdUtility getHouseholdUtilityRandomSampleGenerator() {
        return new HouseholdUtility()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .accountId(UUID.randomUUID().toString());
    }
}
