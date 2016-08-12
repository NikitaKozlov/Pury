package com.nikitakozlov.pury;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ProfileMethodAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.ProfileMethod * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.ProfileMethod *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void method() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructor() {}

    @Around("constructor() || method()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        Log.d("ProfileMethod", "--> " + stopWatch.getExecTimeInMillis() + "ms");

        return result;
    }
}
