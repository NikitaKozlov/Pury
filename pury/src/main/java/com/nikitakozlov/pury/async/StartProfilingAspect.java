package com.nikitakozlov.pury.async;

import com.nikitakozlov.pury.internal.Profiler;
import com.nikitakozlov.pury.internal.ProfilingManager;
import com.nikitakozlov.pury.internal.ProfilerId;
import com.nikitakozlov.pury.internal.StageId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class StartProfilingAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.async.StartProfiling * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.async.StartProfiling *.new(..))";


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
        //for (Profiler profiler : getAllProfilers(joinPoint)) profiler.startRun();
        return joinPoint.proceed();
    }

    private List<Profiler> getAllProfilers(ProceedingJoinPoint joinPoint) {
        Annotation[] annotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
        List<Profiler> profilers = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == StartProfiling.class) {
                profilers.add(getAsyncProfiler((StartProfiling) annotation));
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

    private Profiler getAsyncProfiler(StartProfiling annotation) {
        ProfilerId profilerId = new ProfilerId(annotation.methodId(), annotation.runsCounter());

        StageId stageId = new StageId(profilerId, annotation.stageName(), annotation.stageOrder());
        return ProfilingManager.getInstance().getAsyncProfiler(profilerId);
    }
}
