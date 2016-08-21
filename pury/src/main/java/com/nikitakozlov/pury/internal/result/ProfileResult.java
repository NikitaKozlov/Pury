package com.nikitakozlov.pury.internal.result;

import java.util.List;

public interface ProfileResult {
    List<? extends ProfileResult> getNestedResults();
}
