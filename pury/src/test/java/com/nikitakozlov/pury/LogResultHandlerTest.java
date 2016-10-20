package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.AverageProfileResult;
import com.nikitakozlov.pury.result.model.AverageTime;
import com.nikitakozlov.pury.result.model.RootAverageProfileResult;
import com.nikitakozlov.pury.result.model.RootSingleProfileResult;
import com.nikitakozlov.pury.result.model.SingleProfileResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LogResultHandlerTest {

    @Mock
    Logger mLogger;

    @Before
    public void setUp() {
        initMocks(this);
        Pury.setLogger(mLogger);
    }

    @Test
    public void handleResult_ShouldLogProperly_WhenRunCounterIsOne() {
        String expectedResult = "Profiling results for App Start:\n" +
                "App Start --> 0ms\n" +
                "  Splash Screen --> 9ms\n" +
                "  Splash Screen <-- 1043ms, execution = 1034ms\n" +
                "  Main Activity Launch --> 1044ms\n" +
                "    onCreate() --> 1082ms\n" +
                "    onCreate() <-- 1149ms, execution = 67ms\n" +
                "  Main Activity Launch <-- 1241ms, execution = 197ms\n" +
                "App Start <-- 1242ms";
        ProfilerId profilerId = new ProfilerId("App Start", 1);

        SingleProfileResult splashScreenProfileResult = new SingleProfileResult("Splash Screen", 9000000L,
                1034000000L, Collections.<SingleProfileResult>emptyList(), 1);

        SingleProfileResult onCreateProfileResult = new SingleProfileResult("onCreate()", 1082000000L,
                67000000L, Collections.<SingleProfileResult>emptyList(), 2);
        SingleProfileResult mainActivityLaunchProfileResult = new SingleProfileResult("Main Activity Launch", 1044000000L,
                197000000L, Collections.singletonList(onCreateProfileResult), 1);

        RootSingleProfileResult appStartProfileResult = new RootSingleProfileResult("App Start",
                1242000000, Arrays.asList(splashScreenProfileResult, mainActivityLaunchProfileResult));

        LogResultHandler logResultHandler = new LogResultHandler();

        logResultHandler.handleResult(appStartProfileResult, profilerId);

        verify(mLogger).result(Pury.LOG_TAG, expectedResult);
    }

    @Test
    public void handleResult_ShouldNotLogProfileName_WhenProfileNameIsEmpty() {
        String expectedResult = "Profiling results:\n" +
                "App Start --> 0ms\n" +
                "App Start <-- 1242ms";
        ProfilerId profilerId = new ProfilerId("", 1);

        RootSingleProfileResult appStartProfileResult = new RootSingleProfileResult("App Start",
                1242000000, Collections.<SingleProfileResult>emptyList());

        LogResultHandler logResultHandler = new LogResultHandler();

        logResultHandler.handleResult(appStartProfileResult, profilerId);

        verify(mLogger).result(Pury.LOG_TAG, expectedResult);
    }


    @Test
    public void handleResult_ShouldLogProperly_WhenRunCounterIsFive() {
        String expectedResult = "Profiling results for Pagination:\n" +
                "Get Next Page --> 0ms\n" +
                "  Load --> avg = 1.08ms, min = 0ms, max = 2ms, for 5 runs\n" +
                "    ApiCall --> avg = 1.10ms, min = 0ms, max = 2ms, for 5 runs\n" +
                "    ApiCall <-- avg = 160.05ms, min = 130ms, max = 200ms, for 5 runs\n" +
                "  Load <-- avg = 260.97ms, min = 241ms, max = 285ms, for 5 runs\n" +
                "Get Next Page <-- avg = 380.11ms, min = 347ms, max = 406ms, for 5 runs";

        ProfilerId profilerId = new ProfilerId("Pagination", 5);

        AverageTime apiCallStartAvgTime = new AverageTime(1100000, 0, 2000000L, 5);
        AverageTime apiCallExecAvgTime = new AverageTime(160050000, 130000000L, 200000000L, 5);
        AverageProfileResult apiCallProfileResult = new AverageProfileResult("ApiCall",
                apiCallStartAvgTime, apiCallExecAvgTime, Collections.<AverageProfileResult>emptyList(), 2);

        AverageTime loadStartAvgTime = new AverageTime(1080000, 0, 2000000L, 5);
        AverageTime loadExecAvgTime = new AverageTime(260970000, 241000000L, 285000000L, 5);
        AverageProfileResult loadProfileResult = new AverageProfileResult("Load", loadStartAvgTime,
                loadExecAvgTime, Collections.singletonList(apiCallProfileResult), 1);

        AverageTime getNextPageAvgTime = new AverageTime(380110000, 347000000L, 406000000L, 5);
        RootAverageProfileResult rootAverageProfileResult = new RootAverageProfileResult("Get Next Page",
                getNextPageAvgTime, Collections.singletonList(loadProfileResult));

        LogResultHandler logResultHandler = new LogResultHandler();

        logResultHandler.handleResult(rootAverageProfileResult, profilerId);

        verify(mLogger).result(Pury.LOG_TAG, expectedResult);
    }

    @After
    public void tearDown() {
        Pury.setLogger(null);
    }

}