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
        return new RootSingleProfileResult(rootStage.getName(), rootStage.getExecTimeInMillis(),
                processStageList(rootStage.getStages(), rootStage.getStartTimeInMillis(), 1));
    }

    private List<SingleProfileResult> processStageList(List<Stage> stages, long rootStartTime, int depth) {
        if (stages.isEmpty()) {
            return Collections.emptyList();
        }
        List<SingleProfileResult> results = new ArrayList<>(stages.size());
        for (Stage stage : stages) {
            results.add(processSingleStage(stage, rootStartTime, depth));
        }
        return results;
    }

    private SingleProfileResult processSingleStage(Stage stage, long rootStartTime, int depth) {
        long relativeStartTime = stage.getStartTimeInMillis() - rootStartTime;

        return new SingleProfileResult(stage.getName(), relativeStartTime,
                stage.getExecTimeInMillis(), processStageList(stage.getStages(),
                rootStartTime, depth + 1), depth);
    }

    private ProfileResult takeAverage(List<RootSingleProfileResult> singleProfileResults) {
        return new RootAverageProfileResult(getAverageExecTime(singleProfileResults),
                getAverageProfileResults(transpose(singleProfileResults)));
    }

    private List<List<SingleProfileResult>> transpose(List<? extends ProfileResult> inputResults) {
        List<List<SingleProfileResult>> outputResult = new ArrayList<>();
        int nestedResultsCounter = inputResults.get(0).getNestedResults().size();
        for (int i = 0; i < nestedResultsCounter; i++) {
            List<SingleProfileResult> resultRow = new ArrayList<>(inputResults.size());
            for (ProfileResult inputResult : inputResults) {
                resultRow.add((SingleProfileResult) inputResult.getNestedResults().get(i));
            }
            outputResult.add(resultRow);
        }
        return outputResult;
    }

    /**
     *
     * @param inputResults transposed.
     * @return average
     */
    private List<AverageProfileResult> getAverageProfileResults(List<List<SingleProfileResult>> inputResults) {
        if (inputResults.isEmpty()) {
            return Collections.emptyList();
        }

        List<AverageProfileResult> results = new ArrayList<>();
        for (List<SingleProfileResult> profileResults : inputResults) {
            results.add(new AverageProfileResult(getAverageStartTime(profileResults),
                    getAverageExecTime(profileResults),
                    getAverageProfileResults(transpose(profileResults)))
            );
        }

        return results;
    }

    private static AverageTime getAverageStartTime(List<? extends ProfileResult> singleProfileResults) {
        List<Long> times = new ArrayList<>(singleProfileResults.size());
        for (ProfileResult profileResult : singleProfileResults) {
            times.add(((SingleProfileResult) profileResult).getStartTime());
        }
        return getAverageTime(times);
    }

    private static AverageTime getAverageExecTime(List<? extends ProfileResult> singleProfileResults) {
        List<Long> times = new ArrayList<>(singleProfileResults.size());
        for (ProfileResult profileResult : singleProfileResults) {
            times.add(((SingleProfileResult) profileResult).getExecTime());
        }
        return getAverageTime(times);
    }

    private static AverageTime getAverageTime(List<Long> times) {
        if (times.isEmpty()) {
            return new AverageTime(0, 0, 0, 0);
        }
        int runCounter = times.size();
        long min = Collections.min(times);
        long max = Collections.max(times);
        return new AverageTime(getAverage(times), min, max, runCounter);
    }

    private static double getAverage(List<Long> longs) {
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
