package com.nikitakozlov.pury.internal.result;

import com.nikitakozlov.pury.internal.profile.Run;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNull;

public class ProfileResultProcessorTest {

    @Test
    public void process_ReturnsNullIfListIsNull() {
        ProfileResultProcessor processor = new ProfileResultProcessor();
        assertNull(processor.process(null));
    }

    @Test
    public void process_ReturnsNullIfListIsEmpty() {
        ProfileResultProcessor processor = new ProfileResultProcessor();
        assertNull(processor.process(Collections.<Run>emptyList()));
    }


}