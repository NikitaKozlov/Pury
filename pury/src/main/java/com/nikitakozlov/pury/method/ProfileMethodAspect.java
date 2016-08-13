package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.internal.MethodProfiler;
import com.nikitakozlov.pury.internal.MethodProfilingManager;
import com.nikitakozlov.pury.internal.ProfilerId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ProfileMethodAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.method.ProfileMethod * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.method.ProfileMethod *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void method() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructor() {}

    @Around("constructor() || method()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        ProfilerId profilerId = new ProfilerId("OnCreate", 10);
        MethodProfiler methodProfiler = MethodProfilingManager.getInstance().getMethodProfiler(profilerId);

        Integer runId = methodProfiler.startRun();
        Object result = joinPoint.proceed();
        methodProfiler.stopRun(runId);

        return result;
    }
}
