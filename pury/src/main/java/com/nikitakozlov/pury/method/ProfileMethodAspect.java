package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.async.StartProfiling;
import com.nikitakozlov.pury.internal.profile.ProfilerId;
import com.nikitakozlov.pury.internal.profile.ProfilingManager;
import com.nikitakozlov.pury.internal.profile.StageId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
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
                StageId stageId = getStageId((ProfileMethod) annotation, joinPoint);
                if (stageId != null) {
                    stageIds.add(stageId);
                }
            }
            if (annotation.annotationType() == ProfileMethods.class) {
                for (ProfileMethod profileMethod : ((ProfileMethods) annotation).value()) {
                    StageId stageId = getStageId(profileMethod, joinPoint);
                    if (stageId != null) {
                        stageIds.add(stageId);
                    }
                }
            }
        }
        return stageIds;
    }

    private StageId getStageId(ProfileMethod annotation, ProceedingJoinPoint joinPoint) {
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
