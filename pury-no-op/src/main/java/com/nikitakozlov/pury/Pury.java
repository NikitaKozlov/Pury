package com.nikitakozlov.pury;


import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.profile.ProfilingManager;

public final class Pury {

    public static final String LOG_TAG = "Pury";
    public static final String LOGGER_PLUGIN = "LoggerPlugin";

    public static void setLogger(Logger logger) {
    }

    public synchronized static Logger getLogger() {
        return null;
    }

    public static boolean isEnabled() {
        return false;
    }

    public synchronized static void setEnabled(boolean enabled) {
    }

    /**
     * @param profilerName used to identify profiler. Used in results.
     * @param stageName    Name of stage to start. Used in results.
     * @param stageOrder   Stage order must be bigger then order of current most nested active stage.
     *                     First profiling must starts with value 0.
     * @param runsCounter  used to identify profiler. Amount of runs to average.
     *                     Result will be available only after all runs are stopped.
     */
    public static void startProfiling(String profilerName, String stageName, int stageOrder,
                                      int runsCounter) {
    }

    /**
     * @param profilerName used to identify profiler. Used in results.
     * @param stageName    Name of stage to stop. Used in results.
     * @param runsCounter  used to identify profiler. Amount of runs to average.
     *                     Result will be available only after all runs are stopped.
     */
    public static void stopProfiling(String profilerName, String stageName, int runsCounter) {
    }

    /**
     * Adding custom Plugins allows to customize result processing. For example instead of
     * logging result send it somewhere, or save it in a permanent storage. Default
     * {@link Plugin} logs result into {@link Logger}.
     *
     * @param key           that identify Plugin
     * @param plugin Plugin to process result
     */
    public static void addPlugin(String key, Plugin plugin) {
    }

    /**
     * Remove {@link Plugin} by key. To remove default result handler use {@link #LOGGER_PLUGIN}
     *
     * @param key that identify Plugin to remove.
     */
    public static void removePlugin(String key) {
    }

    /**
     * {@link #startProfiling(String, String, int, int)} and {@link #stopProfiling(String, String, int)}
     * creates {@link ProfilerId} on every call. To avoid creating same ProfilerId multiple times,
     * it is possible to call ProfilingManager directly.
     * @return {@link ProfilingManager}
     */
    public static ProfilingManager getProfilingManager() {
        return null;
    }

    static void setProfilingManager(ProfilingManager profilingManager) {
    }
}
