package com.nikitakozlov.pury.aspects;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.PurySetter;
import com.nikitakozlov.pury.annotations.MethodProfiling;
import com.nikitakozlov.pury.annotations.MethodProfilings;
import com.nikitakozlov.pury.profile.Profiler;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MethodProfilingAspectTest {

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
    private MethodProfilingAspect aspect;

    @Before
    public void setUp() {
        profilingManager = mock(ProfilingManager.class);
        PurySetter.setProfilingManager(profilingManager);
        aspect = new MethodProfilingAspect();
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromMethodProfilingAnnotationAndStartProfiler() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId))).thenReturn(profiler);

        aspect.weaveJoinPoint(mockProceedingJoinPoint("methodWithMethodProfilingAnnotation"));

        verify(profilingManager, times(2)).getProfiler(eq(profilerId));
        verify(profiler).startStage(STAGE_NAME_1, STAGE_ORDER_1);
        verify(profiler).stopStage(STAGE_NAME_1);
    }

    @Test
    public void weaveJoinPoint_CreatesDefaultStageNameAndStartProfiler_WhenMethodProfilingAnnotationHasNoStageName() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);
        Profiler profiler = mock(Profiler.class);
        when(profilingManager.getProfiler(eq(profilerId))).thenReturn(profiler);

        String methodName = "methodWithMethodProfilingAnnotationWithoutStageName";
        String defaultStageName = this.getClass().getSimpleName() + "." + methodName;

        aspect.weaveJoinPoint(mockProceedingJoinPoint(methodName));

        verify(profilingManager, times(2)).getProfiler(eq(profilerId));
        verify(profiler).startStage(defaultStageName, STAGE_ORDER_1);
        verify(profiler).stopStage(defaultStageName);
    }

    @Test
    public void weaveJoinPoint_DoesNothing_WhenMethodProfilingAnnotationIsDisabled() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);

        aspect.weaveJoinPoint(mockProceedingJoinPoint("methodWithDisabledMethodProfilingAnnotation"));

        verify(profilingManager, never()).getProfiler(eq(profilerId));
    }

    @Test
    public void weaveJoinPoint_DoesNothing_WhenPuryIsDisabled() throws Throwable {
        ProfilerId profilerId = new ProfilerId(PROFILER_NAME_1, RUNS_COUNTER_5);

        Pury.setEnabled(false);
        aspect.weaveJoinPoint(mockProceedingJoinPoint("methodWithMethodProfilingAnnotation"));

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

        aspect.weaveJoinPoint(mockProceedingJoinPoint("methodWithMethodProfilingsAnnotation"));

        verify(profilingManager, times(2)).getProfiler(eq(profilerId1));
        verify(profiler1).startStage(STAGE_NAME_1, STAGE_ORDER_1);
        verify(profiler1).stopStage(STAGE_NAME_1);

        verify(profilingManager, times(2)).getProfiler(eq(profilerId2));
        verify(profiler2).startStage(STAGE_NAME_2, STAGE_ORDER_2);
        verify(profiler2).stopStage(STAGE_NAME_2);
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

        aspect.weaveJoinPoint(mockProceedingJoinPoint("methodWithBothAnnotations"));

        verify(profilingManager, times(2)).getProfiler(eq(profilerId1));
        verify(profiler1).startStage(STAGE_NAME_1, STAGE_ORDER_1);
        verify(profiler1).stopStage(STAGE_NAME_1);

        verify(profilingManager, times(2)).getProfiler(eq(profilerId2));
        verify(profiler2).startStage(STAGE_NAME_2, STAGE_ORDER_2);
        verify(profiler2).stopStage(STAGE_NAME_2);

        verify(profilingManager, times(2)).getProfiler(eq(profilerId3));
        verify(profiler3).startStage(STAGE_NAME_3, STAGE_ORDER_3);
        verify(profiler3).stopStage(STAGE_NAME_3);
    }

    /**
     * This test is made just to remove point cut methods from test coverage diagram.
     */
    @Test
    public void dummyTest() {
        aspect.constructor();
        aspect.method();
        aspect.constructorWithMultipleAnnotations();
        aspect.methodWithMultipleAnnotations();
    }

    @After
    public void tearDown() {
        PurySetter.setProfilingManager(null);
        Pury.setEnabled(true);
    }

    private ProceedingJoinPoint mockProceedingJoinPoint(String methodName) throws NoSuchMethodException {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getDeclaringType()).thenReturn(this.getClass());
        when(methodSignature.getName()).thenReturn(methodName);

        when(methodSignature.getMethod()).thenReturn(this.getClass().getDeclaredMethod(methodName));
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    @MethodProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
            stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1)
    private void methodWithMethodProfilingAnnotation() {
    }

    @MethodProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
            stageOrder = STAGE_ORDER_1)
    private void methodWithMethodProfilingAnnotationWithoutStageName() {
    }

    @MethodProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
            stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1, enabled = false)
    private void methodWithDisabledMethodProfilingAnnotation() {
    }

    @MethodProfilings(value = {
            @MethodProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
                    stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1),
            @MethodProfiling(runsCounter = RUNS_COUNTER_4, profilerName = PROFILER_NAME_2,
                    stageName = STAGE_NAME_2, stageOrder = STAGE_ORDER_2)
    })
    private void methodWithMethodProfilingsAnnotation() {
    }

    @MethodProfilings(value = {
            @MethodProfiling(runsCounter = RUNS_COUNTER_5, profilerName = PROFILER_NAME_1,
                    stageName = STAGE_NAME_1, stageOrder = STAGE_ORDER_1),
            @MethodProfiling(runsCounter = RUNS_COUNTER_4, profilerName = PROFILER_NAME_2,
                    stageName = STAGE_NAME_2, stageOrder = STAGE_ORDER_2)
    })
    @MethodProfiling(runsCounter = RUNS_COUNTER_3, profilerName = PROFILER_NAME_3,
            stageName = STAGE_NAME_3, stageOrder = STAGE_ORDER_3)
    private void methodWithBothAnnotations() {
    }
}