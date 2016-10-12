package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilingManager;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PuryTest {

    @Test
    public void getProfilingManager_ReturnsNotNull() {
        assertNotNull(Pury.getProfilingManager());
    }
}
