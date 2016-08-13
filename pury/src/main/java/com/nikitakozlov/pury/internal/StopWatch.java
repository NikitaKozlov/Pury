package com.nikitakozlov.pury.internal;


import android.util.Log;

public class StopWatch {

    private long startTimeStamp;
    private long execTime;

    public void start() {
        startTimeStamp = System.currentTimeMillis();
    }

    public void stop() {
        execTime = System.currentTimeMillis() - startTimeStamp;
        Log.d("StopWatch", String.valueOf(execTime));
    }

    public long getExecTimeInMillis() {
        return execTime;
    }

}
