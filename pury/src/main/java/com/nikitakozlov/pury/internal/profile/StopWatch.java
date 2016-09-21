package com.nikitakozlov.pury.internal.profile;

public class StopWatch {

    private long startTimeStamp;
    private long execTime;
    private boolean isStopped;

    public void start() {
        if (!isStopped) {
            startTimeStamp = System.currentTimeMillis();
        }
    }

    public void stop() {
        if (!isStopped) {
            execTime = System.currentTimeMillis() - startTimeStamp;
            isStopped = true;
        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    public long getExecTimeInMillis() {
        return execTime;
    }

    public long getStartTimeInMillis() {
        return startTimeStamp;
    }
}
