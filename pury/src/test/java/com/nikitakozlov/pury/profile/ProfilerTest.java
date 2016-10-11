package com.nikitakozlov.pury.profile;


import com.nikitakozlov.pury.Logger;
import com.nikitakozlov.pury.result.ResultManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfilerTest {

    private static final String METHOD_ID = "method id";
    private static final int RUN_COUNTER_1 = 1;
    private static final int RUN_COUNTER_2 = 2;

    private static final String STAGE_0 = "Stage 0";
    private static final String STAGE_1 = "Stage 1";
    private static final String STAGE_2 = "Stage 2";
    private static final int STAGE_ORDER_START_ORDER = Profiler.START_ORDER;
    private static final int STAGE_ORDER_1 = 1;
    private static final int STAGE_ORDER_2 = 2;

    @Mock
    Profiler.Callback mCallback;

    @Mock
    ProfileResultProcessor mResultProcessor;

    @Mock
    Logger mLogger;

    @Mock
    ResultManager mResultManager;

    @Mock
    RunFactory mRunFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startStage_LogWarning_IfNoRunsAndStageOrderNotEqualStartOrder() {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_1, STAGE_ORDER_1);

        verify(mLogger).warning(eq(Profiler.LOG_TAG), anyString());
        verify(mRunFactory, never()).startNewRun(anyString(), anyInt());
    }

    @Test
    public void startStage_StartsRun_IfNoRuns() {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);

        verify(mRunFactory).startNewRun(STAGE_0, STAGE_ORDER_START_ORDER);
    }

    @Test
    public void startStage_StartsSecondRun_IfFirstRunIsStopped() {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_2);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);

        verify(mRunFactory, times(2)).startNewRun(STAGE_0, STAGE_ORDER_START_ORDER);
    }

    @Test
    public void startStage_DontStartsSecondRun_IfFirstRunIsStoppedAndRunCounterIsOne() {
        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);

        verify(mRunFactory).startNewRun(STAGE_0, STAGE_ORDER_START_ORDER);
    }

    @Test
    public void startStage_StartsStage_OnSecondStartStage() {
        Run run = mock(Run.class);
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER)).thenReturn(run);

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.startStage(STAGE_1, STAGE_ORDER_1);

        verify(run).startStage(STAGE_1, STAGE_ORDER_1);
        verify(mLogger, never()).error(anyString(), anyString());
    }

    @Test
    public void startStage_LogsError_OnSecondStartStage_WhenRunReturnsError() {
        StageError stageError = mock(StageError.class);
        //Any type, shouldn't be null.
        when(stageError.getType()).thenReturn(StageError.Type.START_PARENT_STAGE_IS_STOPPED);
        Run run = mock(Run.class);
        when(run.startStage(STAGE_1, STAGE_ORDER_1)).thenReturn(stageError);
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER)).thenReturn(run);

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.startStage(STAGE_1, STAGE_ORDER_1);

        verify(mLogger).error(eq(Profiler.LOG_TAG), anyString());
    }

    @Test
    public void stopStage_CallProcessorAndLogger_WhenRunCounterIsOne() {
        Run run = spy(new Run(STAGE_0, STAGE_ORDER_START_ORDER));
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER)).thenReturn(run);

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);

        verify(mResultProcessor).process(eq(Collections.singletonList(run)));
        verify(mLogger).result(eq(Profiler.LOG_TAG), anyString());
        verify(mCallback).onDone(profilerId);
    }

    @Test
    public void stopStage_CallProcessorAndResultManagerOnSecondCall_WhenRunCounterIsTwo() {
        Run run = spy(new Run(STAGE_0, STAGE_ORDER_START_ORDER));
        Run run1 = spy(new Run(STAGE_0, STAGE_ORDER_START_ORDER));
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER)).thenReturn(run, run1);

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_2);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);

        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);

        verify(mResultProcessor).process(eq(Arrays.asList(run, run1)));
        verify(mLogger).result(eq(Profiler.LOG_TAG), anyString());
        verify(mCallback).onDone(profilerId);
    }

    @Test
    public void stopStage_CallLoggerError_WhenStageIsAlreadyStoppedAndRunCounterIsOne() {
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER))
                .thenReturn(new Run(STAGE_0, STAGE_ORDER_START_ORDER));

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);
        profiler.stopStage(STAGE_0);

        verify(mLogger).error(eq(Profiler.LOG_TAG), anyString());
        verify(mLogger).result(eq(Profiler.LOG_TAG), anyString());
    }

    @Test
    public void stopStage_CallLoggerError_WhenStageIsAlreadyStoppedAndRunCounterIsTwo() {
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER))
                .thenReturn(new Run(STAGE_0, STAGE_ORDER_START_ORDER));

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_2);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_0);
        profiler.stopStage(STAGE_0);

        verify(mLogger).error(eq(Profiler.LOG_TAG), anyString());
        verify(mLogger, never()).result(eq(Profiler.LOG_TAG), anyString());
    }

    @Test
    public void stopStage_CallLoggerError_WhenStageNameIsWrong() {
        when(mRunFactory.startNewRun(STAGE_0, STAGE_ORDER_START_ORDER))
                .thenReturn(new Run(STAGE_0, STAGE_ORDER_START_ORDER));

        ProfilerId profilerId = new ProfilerId(METHOD_ID, RUN_COUNTER_1);
        Profiler profiler = new Profiler(profilerId, mCallback, mResultProcessor, mResultManager, mLogger, mRunFactory);
        profiler.startStage(STAGE_0, STAGE_ORDER_START_ORDER);
        profiler.stopStage(STAGE_1);

        verify(mLogger).error(eq(Profiler.LOG_TAG), anyString());
    }
}