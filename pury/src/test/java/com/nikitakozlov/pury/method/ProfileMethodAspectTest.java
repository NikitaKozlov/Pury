package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.internal.MethodProfileResult;
import com.nikitakozlov.pury.internal.MethodProfiler;
import com.nikitakozlov.pury.internal.MethodProfilingManager;
import com.nikitakozlov.pury.internal.ProfilerId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileMethodAspectTest {

    private static final int DEFAULT_RUNS_COUNTER = 1;
    private static final int RUNS_COUNTER_5 = 5;
    private static final int RUNS_COUNTER_10 = 10;
    private static final String DEFAULT_METHOD_ID = "";
    private static final String METHOD_ID = "methodId";
    private static final MethodProfiler.Callback methodProfilerCallback = new MethodProfiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId, MethodProfileResult result) {

        }
    };

    @Test
    public void weaveJoinPoint_RunsProceedOnce() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithoutAnnotations");
        ProfileMethodAspect aspect = new ProfileMethodAspect();
        aspect.weaveJoinPoint(joinPoint);
        verify(joinPoint).proceed();
    }

    @Test
    public void weaveJoinPoint_ReturnsProceedResult() throws Throwable {
        Object proceedResult = new Object();
        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithoutAnnotations");
        ProfileMethodAspect aspect = new ProfileMethodAspect();
        when(joinPoint.proceed()).thenReturn(proceedResult);
        assertEquals(proceedResult, aspect.weaveJoinPoint(joinPoint));
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromProfileMethodAnnotation() throws Throwable {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUNS_COUNTER_5);
        MethodProfilingManager methodProfilingManager = mock(MethodProfilingManager.class);
        when(methodProfilingManager.getMethodProfiler(eq(profilerId)))
                .thenReturn(new MethodProfiler(profilerId, methodProfilerCallback));

        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithProfileMethodAnnotation");
        ProfileMethodAspect aspect = new ProfileMethodAspect(methodProfilingManager);
        aspect.weaveJoinPoint(joinPoint);
        verify(methodProfilingManager).getMethodProfiler(eq(profilerId));
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromProfileMethodsAnnotation() throws Throwable {
        ProfilerId profilerId10Runs = new ProfilerId(METHOD_ID, RUNS_COUNTER_10);
        ProfilerId profilerId1Run = new ProfilerId(DEFAULT_METHOD_ID, DEFAULT_RUNS_COUNTER);

        MethodProfilingManager methodProfilingManager = mock(MethodProfilingManager.class);
        when(methodProfilingManager.getMethodProfiler(eq(profilerId10Runs)))
                .thenReturn(new MethodProfiler(profilerId10Runs, methodProfilerCallback));
        when(methodProfilingManager.getMethodProfiler(eq(profilerId1Run)))
                .thenReturn(new MethodProfiler(profilerId1Run, methodProfilerCallback));

        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithProfileMethodsAnnotation");
        ProfileMethodAspect aspect = new ProfileMethodAspect(methodProfilingManager);
        aspect.weaveJoinPoint(joinPoint);
        verify(methodProfilingManager).getMethodProfiler(eq(profilerId10Runs));
        verify(methodProfilingManager).getMethodProfiler(eq(profilerId1Run));
    }

    @Test
    public void weaveJoinPoint_TakesParametersFromProfileMethodAndProfileMethodsAnnotations() throws Throwable {
        ProfilerId profilerId5Runs = new ProfilerId(METHOD_ID, RUNS_COUNTER_5);
        ProfilerId profilerId10Runs = new ProfilerId(METHOD_ID, RUNS_COUNTER_10);
        ProfilerId profilerId1Run = new ProfilerId(DEFAULT_METHOD_ID, DEFAULT_RUNS_COUNTER);

        MethodProfilingManager methodProfilingManager = mock(MethodProfilingManager.class);
        when(methodProfilingManager.getMethodProfiler(eq(profilerId5Runs)))
                .thenReturn(new MethodProfiler(profilerId5Runs, methodProfilerCallback));
        when(methodProfilingManager.getMethodProfiler(eq(profilerId10Runs)))
                .thenReturn(new MethodProfiler(profilerId10Runs, methodProfilerCallback));
        when(methodProfilingManager.getMethodProfiler(eq(profilerId1Run)))
                .thenReturn(new MethodProfiler(profilerId1Run, methodProfilerCallback));

        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithProfileMethodAndProfileMethodsAnnotations");
        ProfileMethodAspect aspect = new ProfileMethodAspect(methodProfilingManager);
        aspect.weaveJoinPoint(joinPoint);
        verify(methodProfilingManager).getMethodProfiler(eq(profilerId5Runs));
        verify(methodProfilingManager).getMethodProfiler(eq(profilerId10Runs));
        verify(methodProfilingManager).getMethodProfiler(eq(profilerId1Run));
    }

    @Test
    public void weaveJoinPoint_StartAndStopAllProfilersAccordingToAnnotations()  throws Throwable {
        ProfilerId profilerId5Runs = new ProfilerId(METHOD_ID, RUNS_COUNTER_5);
        MethodProfiler profiler5Runs = mock(MethodProfiler.class);
        Integer profiler5RunsRunId = 1;
        when(profiler5Runs.startRun()).thenReturn(profiler5RunsRunId);

        ProfilerId profilerId10Runs = new ProfilerId(METHOD_ID, RUNS_COUNTER_10);
        MethodProfiler profiler10Runs = mock(MethodProfiler.class);
        Integer profiler10RunsRunId = 2;
        when(profiler10Runs.startRun()).thenReturn(profiler10RunsRunId);

        ProfilerId profilerId1Run = new ProfilerId(DEFAULT_METHOD_ID, DEFAULT_RUNS_COUNTER);
        MethodProfiler profiler1Run = mock(MethodProfiler.class);
        Integer profiler1RunRunId = 3;
        when(profiler1Run.startRun()).thenReturn(profiler1RunRunId);


        MethodProfilingManager methodProfilingManager = mock(MethodProfilingManager.class);
        when(methodProfilingManager.getMethodProfiler(eq(profilerId1Run)))
                .thenReturn(profiler1Run);
        when(methodProfilingManager.getMethodProfiler(eq(profilerId5Runs)))
                .thenReturn(profiler5Runs);
        when(methodProfilingManager.getMethodProfiler(eq(profilerId10Runs)))
                .thenReturn(profiler10Runs);

        ProceedingJoinPoint joinPoint = mockJoinPoint("methodWithProfileMethodAndProfileMethodsAnnotations");
        ProfileMethodAspect aspect = new ProfileMethodAspect(methodProfilingManager);
        aspect.weaveJoinPoint(joinPoint);

        verify(profiler1Run).stopRun(profiler1RunRunId);
        verify(profiler5Runs).stopRun(profiler5RunsRunId);
        verify(profiler10Runs).stopRun(profiler10RunsRunId);
    }

    private ProceedingJoinPoint mockJoinPoint(String methodName) throws NoSuchMethodException {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getMethod()).thenReturn(this.getClass().getDeclaredMethod(methodName));
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    private void methodWithoutAnnotations() {
    }

    @ProfileMethod(runsCounter = RUNS_COUNTER_5, methodId = METHOD_ID)
    private void methodWithProfileMethodAnnotation() {
    }

    @ProfileMethods(value = {
            @ProfileMethod(runsCounter = RUNS_COUNTER_10, methodId = METHOD_ID),
            @ProfileMethod()
    })
    private void methodWithProfileMethodsAnnotation() {
    }

    @ProfileMethods(value = {
            @ProfileMethod(runsCounter = RUNS_COUNTER_10, methodId = METHOD_ID),
            @ProfileMethod()
    })
    @ProfileMethod(runsCounter = RUNS_COUNTER_5, methodId = METHOD_ID)
    private void methodWithProfileMethodAndProfileMethodsAnnotations() {
    }

}