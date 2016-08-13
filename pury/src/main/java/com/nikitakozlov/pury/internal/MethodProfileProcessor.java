package com.nikitakozlov.pury.internal;

import java.util.Collection;

class MethodProfileProcessor {
    static MethodProfileResult process(ProfilerId profilerId, Collection<StopWatch> stopWatches) {
        final int collectionSize = stopWatches.size();
        if (collectionSize <= 0) {
            return null;
        }

        long min = Long.MAX_VALUE;
        long max = 0;
        double average = 0;

        for (StopWatch stopWatch : stopWatches) {
            long execTime = stopWatch.getExecTimeInMillis();
            if (execTime < min) {
                min = execTime;
            }
            if (execTime > max) {
                max = execTime;
            }
            average += ((double) execTime) / collectionSize;
        }
        return new MethodProfileResult(profilerId.getMethodId(), collectionSize, average, min, max);
    }

    private MethodProfileProcessor() {
    }
}
