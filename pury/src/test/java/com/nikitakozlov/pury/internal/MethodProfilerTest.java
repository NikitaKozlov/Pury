package com.nikitakozlov.pury.internal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class MethodProfilerTest {

    @Mock
    MethodProfiler.Callback callback;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void profiler_Should_CallCallback_When_AllRunsAreDone() {
        int runsCounter = 10;
        ProfilerId profilerId = new ProfilerId("methodName", runsCounter);
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, callback);
        for (int i = 0; i < runsCounter; i++) {
            methodProfiler.stopRun(methodProfiler.startRun());
        }
        verify(callback).onDone(profilerId);
    }

    @Test
    public void profiler_ShouldNot_CallCallback_When_NotAllRunsAreDone() {
        int runsCounter = 10;
        ProfilerId profilerId = new ProfilerId("methodName", runsCounter);
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, callback);
        for (int i = 0; i < runsCounter - 1; i++) {
            methodProfiler.stopRun(methodProfiler.startRun());
        }
        verify(callback, never()).onDone(profilerId);
    }

    @Test(expected = IllegalStateException.class)
    public void profiler_ShouldNot_AllowsToStartMoreRunsThenRunsCounter() {
        int runsCounter = 10;
        ProfilerId profilerId = new ProfilerId("methodName", runsCounter);
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, callback);
        for (int i = 0; i < runsCounter; i++) {
            methodProfiler.stopRun(methodProfiler.startRun());
        }
        methodProfiler.startRun();
    }
}