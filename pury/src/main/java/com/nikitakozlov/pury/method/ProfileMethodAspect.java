package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.internal.profile.ProfilerId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class ProfileMethodAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.method.ProfileMethod * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.method.ProfileMethod *.new(..))";


    private static final String GROUP_ANNOTATION_POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.method.ProfileMethods * *(..))";

    private static final String GROUP_ANNOTATION_POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.method.ProfileMethods *.new(..))";

    private final MethodProfilingManager mMethodProfilingManager;

    public ProfileMethodAspect() {
        this(new MethodProfilingManager());
    }

    ProfileMethodAspect(MethodProfilingManager methodProfilingManager) {
        mMethodProfilingManager = methodProfilingManager;
    }

    @Pointcut(POINTCUT_METHOD)
    public void method() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructor() {
    }

    @Pointcut(GROUP_ANNOTATION_POINTCUT_METHOD)
    public void methodWithMultipleAnnotations() {
    }

    @Pointcut(GROUP_ANNOTATION_POINTCUT_CONSTRUCTOR)
    public void constructorWithMultipleAnnotations() {
    }

    @Around("constructor() || method() || methodWithMultipleAnnotations() || constructorWithMultipleAnnotations()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        List<MethodProfiler> profilers = getAllProfilers(joinPoint);
        List<Integer> runIds = new ArrayList<>(profilers.size());

        for (MethodProfiler profiler : profilers) runIds.add(profiler.startRun());
        Object result = joinPoint.proceed();
        for (int i = 0; i < profilers.size(); i++) profilers.get(i).stopRun(runIds.get(i));

        return result;
    }

    private List<MethodProfiler> getAllProfilers(ProceedingJoinPoint joinPoint) {
        Annotation[] annotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
        List<MethodProfiler> profilers = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == ProfileMethod.class) {
                profilers.add(getMethodProfiler((ProfileMethod) annotation));
            } else if (annotation.annotationType() == ProfileMethods.class) {
                ProfileMethods profileMethodsAnnotation = ((ProfileMethods) annotation);
                for (ProfileMethod profileMethod : profileMethodsAnnotation.value()) {
                    profilers.add(getMethodProfiler(profileMethod));
                }
            }
        }
        return profilers;
    }

    private MethodProfiler getMethodProfiler(ProfileMethod profileMethodAnnotation) {
        ProfilerId profilerId = new ProfilerId(profileMethodAnnotation.methodId(),
                profileMethodAnnotation.runsCounter());
        return mMethodProfilingManager.getMethodProfiler(profilerId);
    }
}
