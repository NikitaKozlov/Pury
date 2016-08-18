package com.nikitakozlov.pury.internal;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StageTest {

    private static final String STAGE_NAME = "stage_name";
    private static final int STAGE_ORDER = 0;

    @Test
    public void start_CallsStartOnStopWatch() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME, STAGE_ORDER, stopWatch);
        stage.start();
        verify(stopWatch).start();
    }

    @Test
    public void start_DontCallsSecondTimeStartOnStopWatch() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME, STAGE_ORDER, stopWatch);
        stage.start();
        stage.start();
        verify(stopWatch).start();
    }

    @Test
    public void start_DontCallsSecondTimeStartOnStopWatch_WhenStopIsCalled() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME, STAGE_ORDER, stopWatch);
        stage.start();
        stage.stop(STAGE_NAME);
        stage.start();
        verify(stopWatch).start();
    }
}