package com.nikitakozlov.pury.internal;


public class StopWatch {

    private long startTimeStamp;
    private long execTime;

    public void start() {
        startTimeStamp = System.currentTimeMillis();
    }

    public void stop() {
        execTime = System.currentTimeMillis() - startTimeStamp;
    }

    public long getExecTimeInMillis() {
        return execTime;
    }

}
