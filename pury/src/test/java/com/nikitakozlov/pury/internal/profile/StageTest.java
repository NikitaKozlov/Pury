package com.nikitakozlov.pury.internal.profile;

import org.junit.Test;

import static android.test.MoreAsserts.assertEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        assertNull(stage.stop(STAGE_NAME_0));
        verify(stopWatch).stop();
    }

    @Test
    public void stop_MoveStageIntoStoppedState() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertNull(stage.stop(STAGE_NAME_0));
        assertFalse(stage.isStarted());
        assertTrue(stage.isStopped());
    }

    @Test
    public void stop_StopOnlyNestedStage_IfNameEqualToNestedStagesName() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        assertNull(stage.stop(STAGE_NAME_1));
        assertTrue(stage.getStages().get(0).isStopped());
        assertFalse(stage.isStopped());
    }

    @Test
    public void stop_StopNestedStage_IfNameEqualToCurrentStagesName() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        assertNull(stage.stop(STAGE_NAME_0));
        assertTrue(stage.getStages().get(0).isStopped());
    }

    @Test
    public void stop_ReturnsError_IfAlreadyStopped() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.stop(STAGE_NAME_0);
        assertEquals(StageError.Type.STOPPED_ALREADY, stage.stop(STAGE_NAME_0).getType());
    }

    @Test
    public void stop_ReturnsError_IfNestedStageAlreadyStopped() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        stage.stop(STAGE_NAME_1);
        assertEquals(StageError.Type.STOPPED_ALREADY, stage.stop(STAGE_NAME_1).getType());
    }

    @Test
    public void stop_ReturnsError_IfStageNameDoestMatch() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertEquals(StageError.Type.STOP_NOT_STARTED_STAGE, stage.stop(STAGE_NAME_1).getType());
    }

    @Test
    public void stop_ReturnsError_IfStageNameDoestMatchWholeHierarchy() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        assertEquals(StageError.Type.STOP_NOT_STARTED_STAGE, stage.stop(STAGE_NAME_2).getType());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_WhenStageOrderIsEqualToProvided() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertEquals(StageError.Type.START_TO_SMALL_ORDER,
                stage.startStage(STAGE_NAME_1, STAGE_ORDER_0).getType());
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_WhenStageOrderIsBiggerThenProvided() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_1);
        stage.start();
        assertEquals(StageError.Type.START_TO_SMALL_ORDER,
                stage.startStage(STAGE_NAME_1, STAGE_ORDER_0).getType());
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_IfCurrentStageWasNotStarted() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        assertEquals(StageError.Type.START_PARENT_STAGE_NOT_STARTED,
                stage.startStage(STAGE_NAME_1, STAGE_ORDER_1).getType());
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldNotStartNextStage_IfCurrentStageWasStopped() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        stage.stop(STAGE_NAME_0);
        assertEquals(StageError.Type.START_PARENT_STAGE_IS_STOPPED,
                stage.startStage(STAGE_NAME_1, STAGE_ORDER_1).getType());
        assertEmpty(stage.getStages());
    }

    @Test
    public void startStage_ShouldStartNextStage() {
        Stage stage = new Stage(STAGE_NAME_0, STAGE_ORDER_0);
        stage.start();
        assertNull(stage.startStage(STAGE_NAME_1, STAGE_ORDER_1));
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