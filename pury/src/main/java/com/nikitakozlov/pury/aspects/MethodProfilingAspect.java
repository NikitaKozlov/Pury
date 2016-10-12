package com.nikitakozlov.pury.aspects;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.annotations.MethodProfiling;
import com.nikitakozlov.pury.annotations.MethodProfilings;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;
import com.nikitakozlov.pury.profile.StageId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Aspect
public class MethodProfilingAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.annotations.MethodProfiling * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.annotations.MethodProfiling *.new(..))";


    private static final String GROUP_ANNOTATION_POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.annotations.MethodProfilings * *(..))";

    private static final String GROUP_ANNOTATION_POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.annotations.MethodProfilings *.new(..))";

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
        ProfilingManager profilingManager = Pury.getProfilingManager();

        List<StageId> stageIds = getStageIds(joinPoint);

        for (StageId stageId : stageIds) {
            profilingManager.getProfiler(stageId.getProfilerId())
                    .startStage(stageId.getStageName(), stageId.getStageOrder());
        }

        Object result = joinPoint.proceed();

        for (StageId stageId : stageIds) {
            profilingManager.getProfiler(stageId.getProfilerId())
                    .stopStage(stageId.getStageName());
        }

        return result;
    }

    private List<StageId> getStageIds(ProceedingJoinPoint joinPoint) {
        if (!Pury.isEnabled()) {
            return Collections.emptyList();
        }

        Annotation[] annotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
        List<StageId> stageIds = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == MethodProfiling.class) {
                StageId stageId = getStageId((MethodProfiling) annotation, joinPoint);
                if (stageId != null) {
                    stageIds.add(stageId);
                }
            }
            if (annotation.annotationType() == MethodProfilings.class) {
                for (MethodProfiling methodProfiling : ((MethodProfilings) annotation).value()) {
                    StageId stageId = getStageId(methodProfiling, joinPoint);
                    if (stageId != null) {
                        stageIds.add(stageId);
                    }
                }
            }
        }
        return stageIds;
    }

    private StageId getStageId(MethodProfiling annotation, ProceedingJoinPoint joinPoint) {
        if (!annotation.enabled()) {
            return null;
        }
        ProfilerId profilerId = new ProfilerId(annotation.profilerName(), annotation.runsCounter());
        String stageName = annotation.stageName();
        if (stageName.isEmpty()) {

            CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

            String className = codeSignature.getDeclaringType().getSimpleName();
            String methodName = codeSignature.getName();

            stageName = className + "." + methodName;
        }

        return new StageId(profilerId, stageName, annotation.stageOrder());
    }
}
