package com.nikitakozlov.pury.internal;


import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ProfilingManagerTest {

    private static final String STAGE_NAME_0 = "stage_name_0";
    private static final int STAGE_ORDER_0 = 0;
    private static final String METHOD_ID_1 = "method id 1";
    private static final String METHOD_ID_2 = "method id 2";
    private static final int RUN_COUNT_1 = 1;
    private static final int RUN_COUNT_2 = 2;

    @Test
    public void getInstance_ReturnsNotNull() {
        assertNotNull(ProfilingManager.getInstance());
    }

    @Test
    public void setInstance_SetInstanceOfSingleton() {
        ProfilingManager profilingManager = mock(ProfilingManager.class);
        ProfilingManager.setInstance(profilingManager);
        assertEquals(profilingManager, ProfilingManager.getInstance());
    }

    @Test
    public void getProfiler_ReturnsProfiler() {
        ProfilingManager profilingManager = ProfilingManager.getInstance();
        assertNotNull(profilingManager.getProfiler(new ProfilerId(METHOD_ID_1, RUN_COUNT_1)));
    }

    @Test
    public void getProfiler_ReturnsDifferentProfilersForDifferentIds() {
        ProfilingManager profilingManager = ProfilingManager.getInstance();
        ProfilerId id1 = new ProfilerId(METHOD_ID_1, RUN_COUNT_1);
        ProfilerId id2 = new ProfilerId(METHOD_ID_2, RUN_COUNT_2);
        Profiler profiler1 = profilingManager.getProfiler(id1);
        Profiler profiler2 = profilingManager.getProfiler(id2);
        assertNotEquals(profiler1, profiler2);
    }

    @Test
    public void getProfiler_ReturnsSameProfilerForSameId() {
        ProfilingManager profilingManager = ProfilingManager.getInstance();
        ProfilerId id = new ProfilerId(METHOD_ID_1, RUN_COUNT_1);
        Profiler profiler = profilingManager.getProfiler(id);
        assertEquals(profiler, profilingManager.getProfiler(id));
    }

    @Test
    public void getProfiler_ReturnsDifferentProfilersForSameId_IfFirstProfilerIsDone() {
        ProfilingManager profilingManager = ProfilingManager.getInstance();
        ProfilerId id = new ProfilerId(METHOD_ID_1, RUN_COUNT_1);
        Profiler profiler = profilingManager.getProfiler(id);
        profiler.startStage(STAGE_NAME_0, STAGE_ORDER_0);
        profiler.stopStage(STAGE_NAME_0);
        Profiler profiler2 = profilingManager.getProfiler(id);
        assertNotEquals(profiler, profiler2);
    }

    @After
    public void tearDown() {
        ProfilingManager.setInstance(null);
    }
}