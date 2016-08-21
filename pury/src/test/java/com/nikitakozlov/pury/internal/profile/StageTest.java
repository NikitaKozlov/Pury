package com.nikitakozlov.pury.internal.profile;

import org.junit.Test;

import static android.test.MoreAsserts.assertEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StageTest {

    private static final String STAGE_NAME_0 = "stage_name_0";
    private static final String STAGE_NAME_1 = "stage_name_1";
    private static final String STAGE_NAME_2 = "stage_name_2";
    private static final int STAGE_ORDER_0 = 0;
    private static final int STAGE_ORDER_1 = 1;
    private static final int STAGE_ORDER_2 = 2;

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
    public void start_MoveStageIntoStartedState() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertTrue(stage.isStarted());
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
    public void stop_CallsStopOnStopWatch() {
        StopWatch stopWatch = mock(StopWatch.class);
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0, stopWatch);
        stage.start();
        stage.stop(STAGE_NAME_0);
        verify(stopWatch).stop();
    }

    @Test
    public void stop_MoveStageIntoStoppedState() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.stop(STAGE_NAME_0);
        assertFalse(stage.isStarted());
        assertTrue(stage.isStopped());
    }

    @Test
    public void stop_StopOnlyNestedStage_IfNameEqualToNestedStagesName() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        stage.stop(STAGE_NAME_1);
        assertTrue(stage.getStages().get(0).isStopped());
        assertFalse(stage.isStopped());
    }

    @Test
    public void stop_StopNestedStage_IfNameEqualToCurrentStagesName() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        stage.stop(STAGE_NAME_0);
        assertTrue(stage.getStages().get(0).isStopped());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_WhenStageOrderIsEqualToProvided() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertFalse(stage.startStage(STAGE_NAME_1, STAGE_ORDER_0));
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_WhenStageOrderIsBiggerThenProvided() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_1);
        stage.start();
        assertFalse(stage.startStage(STAGE_NAME_1, STAGE_ORDER_0));
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_IfCurrentStageWasNotStarted() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_1);
        assertFalse(stage.startStage(STAGE_NAME_1, STAGE_ORDER_1));
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_IfCurrentStageWasStopped() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_1);
        stage.start();
        stage.stop(STAGE_NAME_0);
        assertFalse(stage.startStage(STAGE_NAME_1, STAGE_ORDER_1));
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldStartNextStage() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertTrue(stage.startStage(STAGE_NAME_1, STAGE_ORDER_1));
        assertTrue(stage.getStages().get(0).isStarted());
    }

    @Test
    public void startStage_ShouldStartNextStage_IfPreviousIsStopped() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        stage.stop(STAGE_NAME_1);
        stage.startStage(STAGE_NAME_2, STAGE_ORDER_2);
        assertTrue(stage.getStages().get(1).isStarted());
    }

    @Test
    public void startStage_ShouldStartNextStageInNestedStage_IfPreviousIsNotStopped() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        stage.startStage(STAGE_NAME_2, STAGE_ORDER_2);
        Stage nestedStage = stage.getStages().get(0);
        assertTrue(nestedStage.getStages().get(0).isStarted());
    }


}