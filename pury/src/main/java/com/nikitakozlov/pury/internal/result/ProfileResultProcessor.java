package com.nikitakozlov.pury.internal.result;

import com.nikitakozlov.pury.internal.profile.Run;
import com.nikitakozlov.pury.internal.profile.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileResultProcessor {
    public ProfileResult process(List<Run> runs) {
        if (runs.isEmpty()) {
            return null;
        }
        List<RootSingleProfileResult> results = new ArrayList<>();
        for (Run run : runs) {
            results.add(processSingleRun(run));
        }

        if (results.size() == 1) {
            return results.get(0);
        }
        return takeAverage(results);
    }

    private RootSingleProfileResult processSingleRun(Run run) {
        Stage rootStage = run.getRootStage();
        return new RootSingleProfileResult(rootStage.getExecTimeInMillis(),
                processStageList(rootStage.getStages(), rootStage.getStartTimeInMillis()));
    }

    private List<SingleProfileResult> processStageList(List<Stage> stages, long rootStartTime) {
        if (stages.isEmpty()) {
            return Collections.emptyList();
        }
        List<SingleProfileResult> results = new ArrayList<>(stages.size());
        for (Stage stage : stages) {
            results.add(processSingleStage(stage, rootStartTime));
        }
        return results;
    }

    private SingleProfileResult processSingleStage(Stage stage, long rootStartTime) {
        long relativeStartTime = stage.getStartTimeInMillis() - rootStartTime;

        return new SingleProfileResult(relativeStartTime,
                stage.getExecTimeInMillis(), processStageList(stage.getStages(), rootStartTime));
    }

    private ProfileResult takeAverage(List<RootSingleProfileResult> singleProfileResults) {
        List<List<SingleProfileResult>> nestedResults = new ArrayList<>();
        int nestedResultsCounter = singleProfileResults.get(0).getNestedResults().size();
        for (int i = 0; i < nestedResultsCounter; i++) {
            List<SingleProfileResult> resultRow = new ArrayList<>(singleProfileResults.size());
            for (RootSingleProfileResult singleProfileResult : singleProfileResults) {
                resultRow.add((SingleProfileResult) singleProfileResult.getNestedResults().get(i));
            }
            nestedResults.add(resultRow);
        }
        return new RootAverageProfileResult(getAverageExecTime(singleProfileResults),
                Collections.<AverageProfileResult>emptyList());
    }

    private List<AverageProfileResult> getAveregeProfileResults(List<List<SingleProfileResult>> inputResult) {
        if (inputResult.isEmpty()) {
            return Collections.emptyList();
        }

        List<AverageProfileResult> results = new ArrayList<>();

        for (List<SingleProfileResult> singleProfileResults : inputResult) {

        }

        return results;
    }

    private AverageTime getAverageExecTime(List<? extends SingleProfileResult> singleProfileResults) {
        List<Long> times = new ArrayList<>(singleProfileResults.size());
        for (SingleProfileResult singleProfileResult : singleProfileResults) {
            times.add(singleProfileResult.getExecTime());
        }
        return getAverageTime(times);
    }

    private AverageTime getAverageTime(List<Long> times) {
        if (times.isEmpty()) {
            return new AverageTime(0, 0, 0, 0);
        }
        int runCounter = times.size();
        long min = Collections.min(times);
        long max = Collections.max(times);
        return new AverageTime(getAverage(times), min, max, runCounter);
    }

    private double getAverage(List<Long> longs) {
        double average = 0;
        if (!longs.isEmpty()) {
            int size = longs.size();
            for (Long aLong : longs) {
                average += aLong.doubleValue() / size;
            }
            return average;
        }
        return average;
    }
}
