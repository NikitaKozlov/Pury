package com.nikitakozlov.pury.async;

import com.nikitakozlov.pury.internal.ProfilerId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class StopProfilingAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.async.StopProfiling * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.async.StopProfiling *.new(..))";


//    private static final String GROUP_ANNOTATION_POINTCUT_METHOD =
//            "execution(@com.nikitakozlov.pury.method.ProfileMethods * *(..))";
//
//    private static final String GROUP_ANNOTATION_POINTCUT_CONSTRUCTOR =
//            "execution(@com.nikitakozlov.pury.method.ProfileMethods *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void method() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructor() {
    }

//    @Pointcut(GROUP_ANNOTATION_POINTCUT_METHOD)
//    public void methodWithMultipleAnnotations() {
//    }
//
//    @Pointcut(GROUP_ANNOTATION_POINTCUT_CONSTRUCTOR)
//    public void constructorWithMultipleAnnotations() {
//    }

    @Around("constructor() || method()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        for (AsyncProfiler profiler : getAllProfilers(joinPoint)) profiler.stopRun();
        return result;
    }

    private List<AsyncProfiler> getAllProfilers(ProceedingJoinPoint joinPoint) {
        Annotation[] annotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
        List<AsyncProfiler> profilers = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == StopProfiling.class) {
                profilers.add(getAsyncProfiler((StopProfiling) annotation));
            }
//            else if (annotation.annotationType() == ProfileMethods.class) {
//                ProfileMethods profileMethodsAnnotation = ((ProfileMethods) annotation);
//                for (ProfileMethod profileMethod : profileMethodsAnnotation.value()) {
//                    profilers.add(getAsyncProfiler(profileMethod));
//                }
//            }
        }
        return profilers;
    }

    private AsyncProfiler getAsyncProfiler(StopProfiling profileMethodAnnotation) {
        ProfilerId profilerId = new ProfilerId(profileMethodAnnotation.methodId(),
                profileMethodAnnotation.runsCounter());
        return AsyncProfilingManager.getInstance().getAsyncProfiler(profilerId);
    }
}
