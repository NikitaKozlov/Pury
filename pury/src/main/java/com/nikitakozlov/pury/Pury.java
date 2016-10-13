package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;
import com.nikitakozlov.pury.result.ResultManager;

public final class Pury {

    public static final String LOG_TAG = "Pury";
    public static final String LOG_RESULT_HANDLER = "LogResultHandler";

    private static volatile Logger sLogger;
    private static volatile boolean sEnabled = true;

    private static final ResultManager sResultManager;
    private static volatile ProfilingManager sProfilingManager;

    static {
        sResultManager = new ResultManager();
        sResultManager.addResultHandler(LOG_RESULT_HANDLER, new LogResultHandler());
        sProfilingManager = new ProfilingManager(sResultManager);
    }

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
            sProfilingManager.clear();
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
            sProfilingManager.getProfiler(profilerId).startStage(stageName, stageOrder);
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
            sProfilingManager.getProfiler(profilerId).stopStage(stageName);
        }
    }

    public static void addResultHandler(String key, ResultHandler resultHandler) {
        sResultManager.addResultHandler(key, resultHandler);
    }

    public static void removeResultHandler(String key) {
        sResultManager.removeResultHandler(key);
    }

    public static ProfilingManager getProfilingManager() {
        return sProfilingManager;
    }

    static void setProfilingManager(ProfilingManager profilingManager) {
        if (profilingManager == null) {
            sProfilingManager = new ProfilingManager(sResultManager);
        } else {
            sProfilingManager = profilingManager;
        }
    }

}
