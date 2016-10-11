package com.nikitakozlov.pury;


public final class Pury {

    public static void setLogger(Logger logger) { }

    public synchronized static Logger getLogger() {
        return null;
    }

    public static boolean isEnabled() {
        return false;
    }

    public synchronized static void setEnabled(boolean enabled) { }

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
                                      int runsCounter) { }

    /**
     *
     * @param profilerName used to identify profiler. Used in results.
     * @param stageName  Name of stage to stop. Used in results.
     * @param runsCounter used to identify profiler. Amount of runs to average.
     *                    Result will be available only after all runs are stopped.
     */
    public static void stopProfiling(String profilerName, String stageName, int runsCounter) { }
}
