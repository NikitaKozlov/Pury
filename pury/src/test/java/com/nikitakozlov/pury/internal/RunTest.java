package com.nikitakozlov.pury.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RunTest {

    private static final String STAGE_NAME_0 = "stage_name_0";
    private static final int STAGE_ORDER_0 = 0;

    @Test
    public void startRun_ReturnsRunWithStartedStage() {
        Run run = Run.startRun(STAGE_NAME_0, STAGE_ORDER_0);
        Stage rootStage = run.getRootStage();
        assertFalse(run.isStopped());
        assertTrue(rootStage.isStarted());
        assertEquals(STAGE_NAME_0, rootStage.getName());
        assertEquals(STAGE_ORDER_0, rootStage.getOrder());
    }

}