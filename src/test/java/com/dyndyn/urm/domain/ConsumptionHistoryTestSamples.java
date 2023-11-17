package com.dyndyn.urm.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumptionHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConsumptionHistory getConsumptionHistorySample1() {
        return new ConsumptionHistory().id(1L);
    }

    public static ConsumptionHistory getConsumptionHistorySample2() {
        return new ConsumptionHistory().id(2L);
    }

    public static ConsumptionHistory getConsumptionHistoryRandomSampleGenerator() {
        return new ConsumptionHistory().id(longCount.incrementAndGet());
    }
}
