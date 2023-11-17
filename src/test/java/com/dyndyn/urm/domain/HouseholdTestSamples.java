package com.dyndyn.urm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HouseholdTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Household getHouseholdSample1() {
        return new Household().id(1L).address("address1").residents(1);
    }

    public static Household getHouseholdSample2() {
        return new Household().id(2L).address("address2").residents(2);
    }

    public static Household getHouseholdRandomSampleGenerator() {
        return new Household().id(longCount.incrementAndGet()).address(UUID.randomUUID().toString()).residents(intCount.incrementAndGet());
    }
}
