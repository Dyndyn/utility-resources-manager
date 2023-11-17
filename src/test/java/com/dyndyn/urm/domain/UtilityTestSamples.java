package com.dyndyn.urm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Utility getUtilitySample1() {
        return new Utility().id(1L).name("name1");
    }

    public static Utility getUtilitySample2() {
        return new Utility().id(2L).name("name2");
    }

    public static Utility getUtilityRandomSampleGenerator() {
        return new Utility().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
