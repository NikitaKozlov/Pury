package com.nikitakozlov.pury.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Profiling will stop after method/constructor. Stops first stage that match stage name.
 * All nested stages will be stopped as well.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface StopProfiling {

    /**
     * Profiler Name, used in results.
     */
    String profilerName() default "";

    /**
     * Name of stage to start. Used in results.
     */
    String stageName() default "";

    /**
     * Amount of runs to average. Result will be available only after all runs are stopped.
     */
    int runsCounter() default 1;

    /**
     * Set to false if you want to skip this annotation.
     */
    boolean enabled() default true;
}
