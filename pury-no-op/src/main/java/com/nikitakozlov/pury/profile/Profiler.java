package com.nikitakozlov.pury.profile;

public class Profiler {

    /**
     * @param stageName  Name of a stage to start. Used in results.
     * @param stageOrder Stage order must be bigger then order of current most nested active stage.
     *                   First profiling must starts with value 0.
     */
    public synchronized void startStage(String stageName, int stageOrder) {
    }
    /**
     * @param stageName Name of stage to stop. Used in results.
     */
    public synchronized void stopStage(String stageName) {
    }

}
