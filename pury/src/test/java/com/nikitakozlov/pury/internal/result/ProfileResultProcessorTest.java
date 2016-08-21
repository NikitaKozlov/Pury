package com.nikitakozlov.pury.internal.result;

import com.nikitakozlov.pury.internal.profile.Run;
import com.nikitakozlov.pury.internal.profile.Stage;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProfileResultProcessorTest {

    private static final long PAUSE_30 = 30L;
    private static final long PAUSE_50 = 50L;

    private static final String STAGE_NAME_0 = "stage_name_0";
    private static final String STAGE_NAME_1 = "stage_name_1";
    private static final String STAGE_NAME_2 = "stage_name_2";
    private static final int STAGE_ORDER_0 = 0;
    private static final int STAGE_ORDER_1 = 1;
    private static final int STAGE_ORDER_2 = 2;

    private ProfileResultProcessor mProcessor;

    @Before
    public void setUp() {
        mProcessor = new ProfileResultProcessor();
    }

    @Test
    public void process_ReturnsNull_IfListIsEmpty() {
        assertNull(mProcessor.process(Collections.<Run>emptyList()));
    }

    @Test
    public void process_ReturnsRootSingleProfitResultWithProperValues_WhenListHasOneRun() throws InterruptedException {
        Run run = getRunWithoutNestedStages(PAUSE_30);
        ProfileResult result = mProcessor.process(Collections.singletonList(run));
        assertTrue(result instanceof RootSingleProfileResult);
        RootSingleProfileResult rootResult = (RootSingleProfileResult) result;
        assertEquals(run.getRootStage().getExecTimeInMillis(), rootResult.getExecTime());
    }

    @Test
    public void process_ReturnsResultWithNestedStageWithStartTimeFromRunStart_WhenListHasOneRunWithOneNestedStage() throws InterruptedException {
        Run run = Run.startRun(STAGE_NAME_0, STAGE_ORDER_0);
        Thread.sleep(PAUSE_30);
        run.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        run.stopStage(STAGE_NAME_0);

        Stage rootStage = run.getRootStage();
        Stage nestedStage = rootStage.getStages().get(0);
        long relativeStartTime = nestedStage.getStartTimeInMillis() - rootStage.getStartTimeInMillis();

        RootSingleProfileResult result = (RootSingleProfileResult) mProcessor.process(
                Collections.singletonList(run));
        SingleProfileResult nestedResult = (SingleProfileResult) result.getNestedResults().get(0);
        assertEquals(relativeStartTime, nestedResult.getStartTime());
        assertEquals(nestedStage.getExecTimeInMillis(), nestedResult.getExecTime());
    }

    @Test
    public void process_ReturnsAverageOfExecutionTimeForRootStages() throws InterruptedException {
        List<Run> runs = Arrays.asList(getRunWithoutNestedStages(PAUSE_30),
                getRunWithoutNestedStages(PAUSE_50));
        Stage rootStage0 = runs.get(0).getRootStage();
        Stage rootStage1 = runs.get(1).getRootStage();

        AverageTime averageExecTime = calculateAverageTimeOfTwo(rootStage0.getExecTimeInMillis(),
                rootStage1.getExecTimeInMillis());

        ProfileResult result = mProcessor.process(runs);
        assertTrue(result instanceof RootAverageProfileResult);
        RootAverageProfileResult rootResult = (RootAverageProfileResult) result;
        assertEquals(averageExecTime, rootResult.getExecTime());
    }

    @Test
    public void process_ReturnsAverageForNestedStages() throws InterruptedException {
        List<Run> runs = Arrays.asList(getRunWithNestedStage(PAUSE_30),
                getRunWithNestedStage(PAUSE_50));

        Stage rootStage0 = runs.get(0).getRootStage();
        Stage rootStage1 = runs.get(1).getRootStage();

        Stage nestedStage0 = rootStage0.getStages().get(0);
        Stage nestedStage1 = rootStage1.getStages().get(0);

        AverageTime averageStartTime = calculateAverageTimeOfTwo(
                nestedStage0.getStartTimeInMillis() - rootStage0.getStartTimeInMillis(),
                nestedStage1.getStartTimeInMillis() - rootStage1.getStartTimeInMillis());

        AverageTime averageExecTime = calculateAverageTimeOfTwo(nestedStage0.getExecTimeInMillis(),
                nestedStage1.getExecTimeInMillis());

        RootAverageProfileResult result = (RootAverageProfileResult) mProcessor.process(runs);
        AverageProfileResult nestedResult = (AverageProfileResult) result.getNestedResults().get(0);

        assertEquals(averageStartTime, nestedResult.getStartTime());
        assertEquals(averageExecTime, nestedResult.getExecTime());
    }

    private Run getRunWithoutNestedStages(long pause) throws InterruptedException {
        Run run = Run.startRun(STAGE_NAME_0, STAGE_ORDER_0);
        Thread.sleep(pause);
        run.stopStage(STAGE_NAME_0);
        return run;
    }

    private Run getRunWithNestedStage(long pause) throws InterruptedException {
        Run run = Run.startRun(STAGE_NAME_0, STAGE_ORDER_0);
        Thread.sleep(pause);
        run.startStage(STAGE_NAME_1, STAGE_ORDER_1);
        run.stopStage(STAGE_NAME_0);
        return run;
    }

    private AverageTime calculateAverageTimeOfTwo(long time1, long time2) {
        long min = Math.min(time1, time2);
        long max = Math.max(time1, time2);
        double average = time1/2.0 + time2/2.0;
        return new AverageTime(average, min, max, 2);
    }
}