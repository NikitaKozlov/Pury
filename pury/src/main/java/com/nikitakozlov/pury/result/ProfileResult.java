package com.nikitakozlov.pury.result;

import java.util.List;

public interface ProfileResult {
    List<? extends ProfileResult> getNestedResults();
}
