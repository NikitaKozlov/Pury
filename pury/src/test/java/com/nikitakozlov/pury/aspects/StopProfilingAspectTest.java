package com.nikitakozlov.pury.aspects;

import com.nikitakozlov.pury.annotations.StartProfiling;
import com.nikitakozlov.pury.annotations.StartProfilings;
import com.nikitakozlov.pury.annotations.StopProfiling;
import com.nikitakozlov.pury.annotations.StopProfilings;
import com.nikitakozlov.pury.internal.profile.Profiler;
import com.nikitakozlov.pury.internal.profile.ProfilingManager;
import com.nikitakozlov.pury.internal.profile.ProfilerId;
import com.nikitakozlov.pury.internal.profile.ProfilingManagerSetter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StopProfilingAspectTest {

    private static final int RUNS_COUNTER_5 = 5;
    private static final String PROFILER_NAME_1 = "profilerName 1";
    private static final String STAGE_NAME_1 = "stageName 1";


    private static final int RUNS_COUNTER_4 = 4;
    private static final String PROFILER_NAME_2 = "profilerName 2";
    private static final String STAGE_NAME_2 = "stageName 2";


    private static final int RUNS_COUNTER_3 = 3;
    private static final String PROFILER_NAME_3 = "profilerName 3";
    private static final String STAGE_NAME_3 = "stageName 3";

    private ProfilingManager profilingManager;
    private StopProfilingAspect aspect;

    @Before
    public void setUp() {
        profilingManager = mock(ProfilingManager.class);
        ProfilingManagerSetter.setInstance(profilingManager);
        aspect = new StopProfilingAspect();
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromStartProfilingAnnotationAndStartAsyncProfiler() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId))).thenReturn(profiler);

        aspect.weaveJoinPoint(mockJoinPoint("methodWithStopProfilingAnnotation"));

        verify(profilingManager).getProfiler(eq(profilerId));
        verify(profiler).stopStage(STAGE_NAME_1);
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromStopProfilingsAnnotationAndStartProfilers() throws Throwable {
        ProfilerId profilerId1 = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler1 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId1))).thenReturn(profiler1);

        ProfilerId profilerId2 = new ProfilerId(PROFILER_NAME_2, RUNS_COUNTER_4);
        Profiler profiler2 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId2))).thenReturn(profiler2);

        aspect.weaveJoinPoint(mockJoinPoint("methodWithStartProfilingsAnnotation"));

        verify(profilingManager).getProfiler(eq(profilerId1));
        verify(profiler1).stopStage(STAGE_NAME_1);

        verify(profilingManager).getProfiler(eq(profilerId2));
        verify(profiler2).stopStage(STAGE_NAME_2);
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromBothAnnotationsAndStopProfilers() throws Throwable {
        ProfilerId profilerId1 = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler1 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId1))).thenReturn(profiler1);

        ProfilerId profilerId2 = new ProfilerId(PROFILER_NAME_2, RUNS_COUNTER_4);
        Profiler profiler2 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId2))).thenReturn(profiler2);

        ProfilerId profilerId3 = new ProfilerId(PROFILER_NAME_3, RUNS_COUNTER_3);
        Profiler profiler3 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId3))).thenReturn(profiler3);

        aspect.weaveJoinPoint(mockJoinPoint("methodWithBothAnnotations"));

        verify(profilingManager).getProfiler(eq(profilerId1));
        verify(profiler1).stopStage(STAGE_NAME_1);

        verify(profilingManager).getProfiler(eq(profilerId2));
        verify(profiler2).stopStage(STAGE_NAME_2);

        verify(profilingManager).getProfiler(eq(profilerId3));
        verify(profiler3).stopStage(STAGE_NAME_3);
    }


    @After
    public void tearDown() {
        ProfilingManagerSetter.setInstance(null);
    }

    private JoinPoint mockJoinPoint(String methodName) throws NoSuchMethodException {
        JoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getMethod()).thenReturn(this.getClass().getDeclaredMethod(methodName));
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    @StopProfiling(runsCounter = RUNS_COUNTER_5, stageName = STAGE_NAME_1,
            profilerName = PROFILER_NAME_1)
    private void methodWithStopProfilingAnnotation() {
    }

    @StopProfilings(value = {
            @StopProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
                    stageName = STAGE_NAME_1),
            @StopProfiling(runsCounter = RUNS_COUNTER_4, profilerName = PROFILER_NAME_2,
                    stageName = STAGE_NAME_2)
    })
    private void methodWithStartProfilingsAnnotation() {
    }

    @StopProfilings(value = {
            @StopProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
                    stageName = STAGE_NAME_1),
            @StopProfiling(runsCounter = RUNS_COUNTER_4, profilerName = PROFILER_NAME_2,
                    stageName = STAGE_NAME_2)
    })
    @StopProfiling(runsCounter = RUNS_COUNTER_3, profilerName = PROFILER_NAME_3,
            stageName = STAGE_NAME_3)
    private void methodWithBothAnnotations() {
    }

}