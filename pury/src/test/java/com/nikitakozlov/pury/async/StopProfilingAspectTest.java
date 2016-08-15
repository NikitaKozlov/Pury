package com.nikitakozlov.pury.async;

import com.nikitakozlov.pury.internal.ProfilerId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StopProfilingAspectTest {

    private static final int RUNS_COUNTER_5 = 5;
    private static final String METHOD_ID = "methodId";

    @Test
    public void weaveJoinPoint_RunsProceedOnce() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithoutAnnotations");
        StopProfilingAspect aspect = new StopProfilingAspect();
        aspect.weaveJoinPoint(joinPoint);
        verify(joinPoint).proceed();
    }

    @Test
    public void weaveJoinPoint_ReturnsProceedResult() throws Throwable {
        Object proceedResult = new Object();
        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithoutAnnotations");
        StopProfilingAspect aspect = new StopProfilingAspect();
        when(joinPoint.proceed()).thenReturn(proceedResult);
        assertEquals(proceedResult, aspect.weaveJoinPoint(joinPoint));
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromStartProfilingAnnotationAndStartAsyncProfiler() throws Throwable {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUNS_COUNTER_5);
        AsyncProfiler asyncProfiler = mock(AsyncProfiler.class);
        AsyncProfilingManager asyncProfilingManager = mock(AsyncProfilingManager.class);
        when(asyncProfilingManager.getAsyncProfiler(eq(profilerId)))
                .thenReturn(asyncProfiler);
        AsyncProfilingManager.setInstance(asyncProfilingManager);

        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithStopProfilingAnnotation");
        StopProfilingAspect aspect = new StopProfilingAspect();
        aspect.weaveJoinPoint(joinPoint);
        verify(asyncProfilingManager).getAsyncProfiler(eq(profilerId));
        verify(asyncProfiler).stopRun();
    }

    private ProceedingJoinPoint mockJoinPoint(String methodName) throws NoSuchMethodException {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getMethod()).thenReturn(this.getClass().getDeclaredMethod(methodName));
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    public void methodWithoutAnnotations() {}

    @StopProfiling(runsCounter = RUNS_COUNTER_5, methodId = METHOD_ID)
    private void methodWithStopProfilingAnnotation() {}

}