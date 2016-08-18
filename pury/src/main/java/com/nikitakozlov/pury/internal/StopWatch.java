package com.nikitakozlov.pury.internal;


import android.util.Log;

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
            Log.d("StopWatch", String.valueOf(execTime));
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
