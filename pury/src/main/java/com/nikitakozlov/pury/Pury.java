package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;

public final class Pury {
    static volatile Logger sLogger;
    static volatile boolean sEnabled = true;

    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    public synchronized static Logger getLogger() {
        if (sLogger == null) {
            sLogger = new DefaultLogger();
        }
        return sLogger;
    }

    public static boolean isEnabled() {
        return sEnabled;
    }

    public synchronized static void setEnabled(boolean enabled) {
        if (!enabled) {
            ProfilingManager.getInstance().clear();
        }
        sEnabled = enabled;
    }

    /**
     *
     * @param profilerName used to identify profiler. Used in results.
     * @param stageName Name of stage to start. Used in results.
     * @param stageOrder Stage order must be bigger then order of current most nested active stage.
     *                   First profiling must starts with value 0.
     * @param runsCounter used to identify profiler. Amount of runs to average.
     *                    Result will be available only after all runs are stopped.
     */
    public static void startProfiling(String profilerName, String stageName, int stageOrder,
                                      int runsCounter) {
        if (isEnabled()) {
            ProfilerId profilerId = new ProfilerId(profilerName, runsCounter);
            ProfilingManager.getInstance().getProfiler(profilerId).startStage(stageName, stageOrder);
        }
    }

    /**
     *
     * @param profilerName used to identify profiler. Used in results.
     * @param stageName  Name of stage to stop. Used in results.
     * @param runsCounter used to identify profiler. Amount of runs to average.
     *                    Result will be available only after all runs are stopped.
     */
    public static void stopProfiling(String profilerName, String stageName, int runsCounter) {
        if (isEnabled()) {
            ProfilerId profilerId = new ProfilerId(profilerName, runsCounter);
            ProfilingManager.getInstance().getProfiler(profilerId).stopStage(stageName);
        }
    }
}
