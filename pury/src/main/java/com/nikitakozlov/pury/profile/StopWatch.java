package com.nikitakozlov.pury.profile;

public class StopWatch {

    private long startTimeStamp;
    private long execTime;
    private boolean isStopped;

    public void start() {
        if (!isStopped) {
            startTimeStamp = System.nanoTime();
        }
    }

    public void stop() {
        if (!isStopped) {
            execTime = System.nanoTime() - startTimeStamp;
            isStopped = true;
        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    public long getExecTime() {
        return execTime;
    }

    public long getStartTime() {
        return startTimeStamp;
    }
}
