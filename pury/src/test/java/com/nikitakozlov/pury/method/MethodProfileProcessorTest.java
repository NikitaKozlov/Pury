package com.nikitakozlov.pury.method;

import com.nikitakozlov.pury.internal.ProfilerId;
import com.nikitakozlov.pury.internal.StopWatch;
import com.nikitakozlov.pury.method.MethodProfileProcessor;
import com.nikitakozlov.pury.method.MethodProfileResult;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodProfileProcessorTest {

    @Test
    public void process_IsCorrect() {
        long minTime = 10L;
        long maxTime = 20L;
        double averageTime = (minTime + maxTime) / 2.0;

        StopWatch minStopWatch = mock(StopWatch.class);
        when(minStopWatch.getExecTimeInMillis()).thenReturn(minTime);

        StopWatch maxStopWatch = mock(StopWatch.class);
        when(maxStopWatch.getExecTimeInMillis()).thenReturn(maxTime);

        List<StopWatch> stopWatches = Arrays.asList(minStopWatch, maxStopWatch);
        ProfilerId profilerId = new ProfilerId("methodName", stopWatches.size());
        MethodProfileResult expectedResult = new MethodProfileResult(profilerId.getMethodId(),
                stopWatches.size(), averageTime, minTime, maxTime);

        MethodProfileResult actualResult = MethodProfileProcessor.process(profilerId, stopWatches);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void process_ReturnsNull_When_PassingEmptyCollection() {
        assertNull(MethodProfileProcessor.process(null, Collections.<StopWatch>emptyList()));
    }
}