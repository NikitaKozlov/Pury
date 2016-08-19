package com.nikitakozlov.pury.async;

import com.nikitakozlov.pury.internal.Profiler;
import com.nikitakozlov.pury.internal.ProfilingManager;
import com.nikitakozlov.pury.internal.ProfilerId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StartProfilingAspectTest {

    private static final int RUNS_COUNTER_5 = 5;
    private static final String METHOD_ID = "methodId";

    @Test
    public void weaveJoinPoint_RunsProceedOnce() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithoutAnnotations");
        StartProfilingAspect aspect = new StartProfilingAspect();
        aspect.weaveJoinPoint(joinPoint);
        verify(joinPoint).proceed();
    }

    @Test
    public void weaveJoinPoint_ReturnsProceedResult() throws Throwable {
        Object proceedResult = new Object();
        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithoutAnnotations");
        StartProfilingAspect aspect = new StartProfilingAspect();
        when(joinPoint.proceed()).thenReturn(proceedResult);
        assertEquals(proceedResult, aspect.weaveJoinPoint(joinPoint));
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromStartProfilingAnnotationAndStartAsyncProfiler() throws Throwable {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUNS_COUNTER_5);
        Profiler profiler = mock(Profiler.class);
        ProfilingManager asyncProfilingManager = mock(ProfilingManager.class);
        when(asyncProfilingManager.getProfiler(eq(profilerId)))
                .thenReturn(profiler);
        ProfilingManager.setInstance(asyncProfilingManager);

        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithStartProfilingAnnotation");
        StartProfilingAspect aspect = new StartProfilingAspect();
        aspect.weaveJoinPoint(joinPoint);
        verify(asyncProfilingManager).getProfiler(eq(profilerId));
        //verify(profiler).startStage();
    }

    private ProceedingJoinPoint mockJoinPoint(String methodName) throws NoSuchMethodException {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getMethod()).thenReturn(this.getClass().getDeclaredMethod(methodName));
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    public void methodWithoutAnnotations() {}

    @StartProfiling(runsCounter = RUNS_COUNTER_5, methodId = METHOD_ID)
    private void methodWithStartProfilingAnnotation() {}

}