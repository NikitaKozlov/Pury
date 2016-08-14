package com.nikitakozlov.pury.method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileMethodAspectTest {

    @Test
    public void weaveJoinPoint_RunsProceedOnce() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(this.getClass().getDeclaredMethod("methodWithoutAnnotations"));
        ProfileMethodAspect aspect = new ProfileMethodAspect();
        aspect.weaveJoinPoint(joinPoint);
        verify(joinPoint).proceed();
    }

    @Test
    public void weaveJoinPoint_ReturnsProceedResult() throws Throwable {
        Object proceedResult = new Object();
        ProceedingJoinPoint joinPoint = mockJoinPoint(this.getClass().getDeclaredMethod("methodWithoutAnnotations"));
        ProfileMethodAspect aspect = new ProfileMethodAspect();
        when(joinPoint.proceed()).thenReturn(proceedResult);
        assertEquals(proceedResult, aspect.weaveJoinPoint(joinPoint));
    }

    private ProceedingJoinPoint mockJoinPoint(Method method) {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        return joinPoint;
    }

    private void methodWithoutAnnotations() {}

    @ProfileMethod(runsCounter = 5, methodId = "methodId")
    private void methodWithProfileMethodAnnotation() {}

    @ProfileMethods(value = {
            @ProfileMethod(runsCounter = 10, methodId = "methodId"),
            @ProfileMethod()
    })
    private void methodWithProfileMethodsAnnotation() {}

    @ProfileMethods(value = {
            @ProfileMethod(runsCounter = 10, methodId = "methodId"),
            @ProfileMethod()
    })
    @ProfileMethod(runsCounter = 5, methodId = "methodId")
    private void methodWithProfileMethodAndProfileMethodsAnnotations() {}

}