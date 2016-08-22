package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.async.StartProfiling;
import com.nikitakozlov.pury.internal.profile.ProfilerId;
import com.nikitakozlov.pury.internal.profile.ProfilingManager;
import com.nikitakozlov.pury.internal.profile.StageId;

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
        ProfilingManager profilingManager = ProfilingManager.getInstance();

        List<StageId> stageIds = getStageIds(joinPoint);

        for (StageId stageId : stageIds) {
            profilingManager.getProfiler(stageId.getProfilerId())
                    .startStage(stageId.getStageName(), stageId.getStageOrder());
        }

        Object result = joinPoint.proceed();

        for (StageId stageId : getStageIds(joinPoint)) {
            profilingManager.getProfiler(stageId.getProfilerId())
                    .stopStage(stageId.getStageName());
        }

        return result;
    }

    private List<StageId> getStageIds(ProceedingJoinPoint joinPoint) {
        Annotation[] annotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
        List<StageId> stageIds = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == ProfileMethod.class) {
                stageIds.add(getStageId((ProfileMethod) annotation));
            }
        }
        return stageIds;
    }

    private StageId getStageId(ProfileMethod annotation) {
        ProfilerId profilerId = new ProfilerId(annotation.methodId(), annotation.runsCounter());
        return new StageId(profilerId, annotation.stageName(), annotation.stageOrder());
    }
}
