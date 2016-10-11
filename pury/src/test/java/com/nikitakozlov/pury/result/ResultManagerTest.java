package com.nikitakozlov.pury.result;

import com.nikitakozlov.pury.result.model.ProfileResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResultManagerTest {

    private static final String KEY_1 = "key 1";
    private static final String KEY_2 = "key 2";

    @Mock
    ResultHandler resultHandler1;

    @Mock
    ResultHandler resultHandler2;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void dispatchResult_ShouldCallAllResultHandlers() {
        ResultManager resultManager = new ResultManager();
        resultManager.addResultHander(KEY_1, resultHandler1);
        resultManager.addResultHander(KEY_2, resultHandler2);

        ProfileResult result = mock(ProfileResult.class);
        resultManager.dispatchResult(result);
        verify(resultHandler1).handleResult(result);
        verify(resultHandler2).handleResult(result);
    }
}