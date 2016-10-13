package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

public interface ProfileResult {
    List<? extends ProfileResult> getNestedResults();

    void accept(ResultVisitor visitor);
}
