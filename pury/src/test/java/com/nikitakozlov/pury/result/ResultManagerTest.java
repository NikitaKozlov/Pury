package com.nikitakozlov.pury.result;

import com.nikitakozlov.pury.Plugin;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResultManagerTest {

    private static final String KEY_1 = "key 1";
    private static final String KEY_2 = "key 2";

    @Mock
    Plugin plugin1;

    @Mock
    Plugin plugin2;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void dispatchResult_ShouldCallAllPlugins() {
        ProfilerId profilerId = mock(ProfilerId.class);
        ResultManager resultManager = new ResultManager();
        resultManager.addPlugin(KEY_1, plugin1);
        resultManager.addPlugin(KEY_2, plugin2);

        ProfileResult result = mock(ProfileResult.class);
        resultManager.dispatchResult(result, profilerId);
        verify(plugin1).handleResult(result, profilerId);
        verify(plugin2).handleResult(result, profilerId);
    }

    @Test
    public void dispatchResult_ShouldNotCallRemovedPlugin() {
        ProfilerId profilerId = mock(ProfilerId.class);
        ResultManager resultManager = new ResultManager();
        resultManager.addPlugin(KEY_1, plugin1);
        resultManager.addPlugin(KEY_2, plugin2);
        resultManager.removePlugin(KEY_1);

        ProfileResult result = mock(ProfileResult.class);
        resultManager.dispatchResult(result, profilerId);
        verify(plugin1, never()).handleResult(result, profilerId);
        verify(plugin2).handleResult(result, profilerId);
    }
}