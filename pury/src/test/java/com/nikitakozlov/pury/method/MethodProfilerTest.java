package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.internal.profile.ProfilerId;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    public void profiler_CallCallback_When_AllRunsAreDone() {
        int runsCounter = 10;
        ProfilerId profilerId = new ProfilerId("methodName", runsCounter);
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, callback);
        for (int i = 0; i < runsCounter; i++) {
            methodProfiler.stopRun(methodProfiler.startRun());
        }
        verify(callback).onDone(eq(profilerId), Mockito.<MethodProfileResult>any());
    }

    @Test
    public void profiler_DoNotCallCallback_When_notAllRunsAreDone() {
        int runsCounter = 10;
        ProfilerId profilerId = new ProfilerId("methodName", runsCounter);
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, callback);
        for (int i = 0; i < runsCounter - 1; i++) {
            methodProfiler.stopRun(methodProfiler.startRun());
        }
        verify(callback, never()).onDone(eq(profilerId), Mockito.<MethodProfileResult>any());
    }

    @Test(expected = IllegalStateException.class)
    public void profiler_DoNotAllowsToStartMoreRunsThenRunsCounter() {
        int runsCounter = 10;
        ProfilerId profilerId = new ProfilerId("methodName", runsCounter);
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, callback);
        for (int i = 0; i < runsCounter; i++) {
            methodProfiler.stopRun(methodProfiler.startRun());
        }
        methodProfiler.startRun();
    }
}