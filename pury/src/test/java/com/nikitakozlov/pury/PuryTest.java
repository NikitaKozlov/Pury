package com.nikitakozlov.pury;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PuryTest {

    @Test
    public void getProfilingManager_ReturnsNotNull() {
        assertNotNull(Pury.getProfilingManager());
    }
}
