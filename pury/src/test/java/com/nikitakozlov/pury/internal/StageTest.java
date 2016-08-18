package com.nikitakozlov.pury.internal;

import org.junit.Test;

import static android.test.MoreAsserts.assertEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StageTest {

    private static final String STAGE_NAME_0 = "stage_name_0";
    private static final String STAGE_NAME_1 = "stage_name_1";
    private static final int STAGE_ORDER_0 = 0;
    private static final int STAGE_ORDER_1 = 1;

    @Test
    public void start_CallsStartOnStopWatch() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0, stopWatch);
        assertTrue(stage.start());
        verify(stopWatch).start();
    }

    @Test
    public void start_DontCallsSecondTimeStartOnStopWatch() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0, stopWatch);
        assertTrue(stage.start());
        assertFalse(stage.start());
        verify(stopWatch).start();
    }

    @Test
    public void start_DontCallsSecondTimeStartOnStopWatch_WhenStopIsCalled() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0, stopWatch);
        assertTrue(stage.start());
        stage.stop(STAGE_NAME_0);
        assertFalse(stage.start());
        verify(stopWatch).start();
    }

    @Test
    public void startStage_ShouldNotAddStageToList_WhenStageOrderIsEqualToProvided() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertFalse(stage.startStage(STAGE_NAME_1, STAGE_ORDER_0));
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldAddStageToListAndStartIt() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertTrue(stage.startStage(STAGE_NAME_1, STAGE_ORDER_1));
        assertTrue(stage.getStages().get(0).isStarted());
    }
}