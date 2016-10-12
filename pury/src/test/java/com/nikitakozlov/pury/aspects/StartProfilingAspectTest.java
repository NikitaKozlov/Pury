package com.nikitakozlov.pury.aspects;

import com.nikitakozlov.pury.PurySetter;
import com.nikitakozlov.pury.annotations.StartProfiling;
import com.nikitakozlov.pury.annotations.StartProfilings;
import com.nikitakozlov.pury.profile.Profiler;
import com.nikitakozlov.pury.profile.ProfilingManager;
import com.nikitakozlov.pury.profile.ProfilerId;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StartProfilingAspectTest {

    private static final int RUNS_COUNTER_5 = 5;
    private static final String PROFILER_NAME_1 = "profilerName 1";
    private static final int STAGE_ORDER_1 = 1;
    private static final String STAGE_NAME_1 = "stageName 1";


    private static final int RUNS_COUNTER_4 = 4;
    private static final String PROFILER_NAME_2 = "profilerName 2";
    private static final int STAGE_ORDER_2 = 2;
    private static final String STAGE_NAME_2 = "stageName 2";


    private static final int RUNS_COUNTER_3 = 3;
    private static final String PROFILER_NAME_3 = "profilerName 3";
    private static final int STAGE_ORDER_3 = 3;
    private static final String STAGE_NAME_3 = "stageName 3";

    private ProfilingManager profilingManager;
    private StartProfilingAspect aspect;

    @Before
    public void setUp() {
        profilingManager = mock(ProfilingManager.class);
        PurySetter.setProfilingManager(profilingManager);
        aspect = new StartProfilingAspect();
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromStartProfilingAnnotationAndStartProfiler() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId))).thenReturn(profiler);

        aspect.weaveJoinPoint(mockJoinPoint("methodWithStartProfilingAnnotation"));

        verify(profilingManager).getProfiler(eq(profilerId));
        verify(profiler).startStage(STAGE_NAME_1, STAGE_ORDER_1);
    }

    @Test
    public void weaveJoinPoint_DoesNothing_WhenStartProfilingAnnotationIsDisabled() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);

        aspect.weaveJoinPoint(mockJoinPoint("methodWithDisabledStartProfilingAnnotation"));

        verify(profilingManager, never()).getProfiler(eq(profilerId));
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromStartProfilingsAnnotationAndStartProfilers() throws Throwable {
        ProfilerId profilerId1 = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler1 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId1))).thenReturn(profiler1);

        ProfilerId profilerId2 = new ProfilerId(PROFILER_NAME_2, RUNS_COUNTER_4);
        Profiler profiler2 = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId2))).thenReturn(profiler2);

        aspect.weaveJoinPoint(mockJoinPoint("methodWithStartProfilingsAnnotation"));

        verify(profilingManager).getProfiler(eq(profilerId1));
        verify(profiler1).startStage(STAGE_NAME_1, STAGE_ORDER_1);

        verify(profilingManager).getProfiler(eq(profilerId2));
        verify(profiler2).startStage(STAGE_NAME_2, STAGE_ORDER_2);
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromBothAnnotationsAndStartProfilers() throws Throwable {
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
        verify(profiler1).startStage(STAGE_NAME_1, STAGE_ORDER_1);

        verify(profilingManager).getProfiler(eq(profilerId2));
        verify(profiler2).startStage(STAGE_NAME_2, STAGE_ORDER_2);

        verify(profilingManager).getProfiler(eq(profilerId3));
        verify(profiler3).startStage(STAGE_NAME_3, STAGE_ORDER_3);
    }

    @After
    public void tearDown() {
        PurySetter.setProfilingManager(null);
    }

    private JoinPoint mockJoinPoint(String methodName) throws NoSuchMethodException {
        JoinPoint joinPoint = mock(JoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getMethod()).thenReturn(this.getClass().getDeclaredMethod(methodName));
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    @StartProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
            stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1)
    private void methodWithStartProfilingAnnotation() {
    }

    @StartProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
            stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1, enabled = false)
    private void methodWithDisabledStartProfilingAnnotation() {
    }

    @StartProfilings(value = {
            @StartProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
                    stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1),
            @StartProfiling(runsCounter = RUNS_COUNTER_4, profilerName = PROFILER_NAME_2,
                    stageName = STAGE_NAME_2, stageOrder = STAGE_ORDER_2)
    })
    private void methodWithStartProfilingsAnnotation() {
    }

    @StartProfilings(value = {
        @StartProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
                stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1),
        @StartProfiling(runsCounter = RUNS_COUNTER_4, profilerName = PROFILER_NAME_2,
                stageName = STAGE_NAME_2, stageOrder = STAGE_ORDER_2)
    })
    @StartProfiling(runsCounter = RUNS_COUNTER_3, profilerName = PROFILER_NAME_3,
            stageName = STAGE_NAME_3, stageOrder = STAGE_ORDER_3)
    private void methodWithBothAnnotations() {
    }
}