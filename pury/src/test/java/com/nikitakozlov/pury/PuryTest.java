package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.Profiler;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PuryTest {

    private static final int RUNS_COUNTER_5 = 5;
    private static final String PROFILER_NAME= "profilerName";
    private static final int STAGE_ORDER = 1;
    private static final String STAGE_NAME = "stageName";

    @Mock
    ProfilingManager mProfilingManager;

    @Mock
    Profiler mProfiler;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getProfilingManager_ReturnsNotNull() {
        assertNotNull(Pury.getProfilingManager());
    }

    @Test
    public void startProfiling_GetsProfilerAndStartsStage() {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME, RUNS_COUNTER_5);
        when(mProfilingManager.getProfiler(profilerId)).thenReturn(mProfiler);
        PurySetter.setProfilingManager(mProfilingManager);

        Pury.startProfiling(PROFILER_NAME, STAGE_NAME, STAGE_ORDER, RUNS_COUNTER_5);

        verify(mProfilingManager).getProfiler(profilerId);
        verify(mProfiler).startStage(STAGE_NAME, STAGE_ORDER);
    }

    @Test
    public void startProfiling_DoesNothing_WhenPuryIsDisabled() {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME, RUNS_COUNTER_5);
        PurySetter.setProfilingManager(mProfilingManager);

        Pury.setEnabled(false);
        Pury.startProfiling(PROFILER_NAME, STAGE_NAME, STAGE_ORDER, RUNS_COUNTER_5);

        verify(mProfilingManager, never()).getProfiler(profilerId);
    }

    @Test
    public void stopProfiling_GetsProfilerAndStopsStage() {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME, RUNS_COUNTER_5);
        when(mProfilingManager.getProfiler(profilerId)).thenReturn(mProfiler);
        PurySetter.setProfilingManager(mProfilingManager);

        Pury.stopProfiling(PROFILER_NAME, STAGE_NAME, RUNS_COUNTER_5);

        verify(mProfilingManager).getProfiler(profilerId);
        verify(mProfiler).stopStage(STAGE_NAME);
    }

    @Test
    public void stopProfiling_DoesNothing_WhenPuryIsDisabled() {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME, RUNS_COUNTER_5);
        PurySetter.setProfilingManager(mProfilingManager);

        Pury.setEnabled(false);
        Pury.stopProfiling(PROFILER_NAME, STAGE_NAME, RUNS_COUNTER_5);

        verify(mProfilingManager, never()).getProfiler(profilerId);
    }

    @After
    public void tearDown() {
        PurySetter.setProfilingManager(null);
        Pury.setEnabled(true);
    }
}
