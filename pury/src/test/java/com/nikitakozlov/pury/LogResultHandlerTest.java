package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
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

    @After
    public void tearDown() {
        Pury.setLogger(null);
    }

}