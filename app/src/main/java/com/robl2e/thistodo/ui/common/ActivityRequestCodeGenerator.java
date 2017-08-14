package com.robl2e.thistodo.ui.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by robl2e on 8/13/2017.
 */
public class ActivityRequestCodeGenerator {
    private static final AtomicInteger seed = new AtomicInteger(100);
    public static int getFreshInt() {
        return seed.incrementAndGet();
    }
}