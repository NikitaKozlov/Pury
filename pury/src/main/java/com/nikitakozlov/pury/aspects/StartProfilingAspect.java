package com.nikitakozlov.pury.aspects;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.annotations.StartProfiling;
import com.nikitakozlov.pury.annotations.StartProfilings;
import com.nikitakozlov.pury.profile.ProfilingManager;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.StageId;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class StartProfilingAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.annotations.StartProfiling * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.annotations.StartProfiling *.new(..))";


    private static final String GROUP_ANNOTATION_POINTCUT_METHOD =
            "execution(@com.nikitakozlov.pury.annotations.StartProfilings * *(..))";


    private static final String GROUP_ANNOTATION_POINTCUT_CONSTRUCTOR =
            "execution(@com.nikitakozlov.pury.annotations.StartProfilings *.new(..))";

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

    @Before("constructor() || method() || methodWithMultipleAnnotations() || constructorWithMultipleAnnotations()")
    public void weaveJoinPoint(JoinPoint joinPoint) throws Throwable {
        if (!Pury.isEnabled()) {
            return;
        }

        ProfilingManager profilingManager = ProfilingManager.getInstance();
        for (StageId stageId : getStageIds(joinPoint)) {
            profilingManager.getProfiler(stageId.getProfilerId())
                    .startStage(stageId.getStageName(), stageId.getStageOrder());
        }
    }

    private List<StageId> getStageIds(JoinPoint joinPoint) {
        Annotation[] annotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
        List<StageId> stageIds = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == StartProfiling.class) {
                StageId stageId = getStageId((StartProfiling) annotation);
                if (stageId != null) {
                    stageIds.add(stageId);
                }
            }
            if (annotation.annotationType() == StartProfilings.class) {
                for (StartProfiling startProfiling : ((StartProfilings) annotation).value()) {
                    StageId stageId = getStageId(startProfiling);
                    if (stageId != null) {
                        stageIds.add(stageId);
                    }
                }
            }
        }
        return stageIds;
    }

    private StageId getStageId(StartProfiling annotation) {
        if (!annotation.enabled()) {
            return null;
        }
        ProfilerId profilerId = new ProfilerId(annotation.profilerName(), annotation.runsCounter());
        return new StageId(profilerId, annotation.stageName(), annotation.stageOrder());
    }
}
