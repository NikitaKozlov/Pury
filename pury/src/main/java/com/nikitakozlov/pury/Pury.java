package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;
import com.nikitakozlov.pury.result.ResultManager;

/**
 * Main interface for interacting with Pury.
 */
public final class Pury {

    public static final String LOG_TAG = "Pury";
    public static final String LOG_RESULT_HANDLER = "LoggerPlugin";

    private static volatile Logger sLogger;
    private static volatile boolean sEnabled = true;

    private static final ResultManager sResultManager;
    private static volatile ProfilingManager sProfilingManager;

    static {
        sResultManager = new ResultManager();
        sResultManager.addPlugin(LOG_RESULT_HANDLER, new LoggerPlugin());
        sProfilingManager = new ProfilingManager(sResultManager);
    }

    /**
     * Set logger for Pury to use. Default Plugin will also use it for output.
     * @param logger Logger to set.
     */
    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    /**
     * @return Logger that was set previously. Or a default one if no logger was set.
     */
    public synchronized static Logger getLogger() {
        if (sLogger == null) {
            sLogger = new DefaultLogger();
        }
        return sLogger;
    }

    /**
     * @return true if Pury is enabled, false otherwise.
     */
    public static boolean isEnabled() {
        return sEnabled;
    }

    /**
     * @param enabled pass false if you want to disable Pury.
     */
    public synchronized static void setEnabled(boolean enabled) {
        if (!enabled) {
            sProfilingManager.clear();
        }
        sEnabled = enabled;
    }

    /**
     *
     * @param profilerName used to identify profiler. Used in results.
     * @param stageName Name of a stage to start. Used in results.
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
     * @param stageName  Name of a stage to stop. Used in results.
     * @param runsCounter used to identify profiler. Amount of runs to average.
     *                    Result will be available only after all runs are stopped.
     */
    public static void stopProfiling(String profilerName, String stageName, int runsCounter) {
        if (isEnabled()) {
            ProfilerId profilerId = new ProfilerId(profilerName, runsCounter);
            sProfilingManager.getProfiler(profilerId).stopStage(stageName);
        }
    }

    /**
     * Adding custom Plugins allows to customize result processing. For example instead of
     * logging result send it somewhere, or save it in a permanent storage. Default
     * {@link Plugin} logs result into {@link Logger}.
     * @param key that identify Plugin
     * @param plugin Plugin to process result
     */
    public static void addPlugin(String key, Plugin plugin) {
        sResultManager.addPlugin(key, plugin);
    }

    /**
     * Remove {@link Plugin} by key. To remove default result handler use {@link #LOG_RESULT_HANDLER}
     * @param key that identify Plugin to remove.
     */
    public static void removePlugin(String key) {
        sResultManager.removePlugin(key);
    }

    /**
     * {@link #startProfiling(String, String, int, int)} and {@link #stopProfiling(String, String, int)}
     * creates {@link ProfilerId} on every call. To avoid creating same ProfilerId multiple times,
     * it is possible to call ProfilingManager directly.
     * @return {@link ProfilingManager}
     */
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
