package com.nikitakozlov.pury;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DebugAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.DebugLog * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.DebugLog *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugLog() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedDebugLog() {}

    @Around("methodAnnotatedWithDebugLog() || constructorAnnotatedDebugLog()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

        Log.d("Aspect", "Logged");

        return joinPoint.proceed();
    }
}
