package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represents profiling result of a single stage.
 */
public interface ProfileResult {

    /**
     * Results preserve nesting structure of a stages. So that if a given stage has tho nested stages,
     * than corresponding result also has two results that are correspond to those nested stages.
     * @return list of nested results.
     */
    List<? extends ProfileResult> getNestedResults();

    /**
     * @return name of a corresponding stage.
     */
    String getStageName();

    /**
     * @return level in the hierarchy of nested stages.
     */
    int getDepth();

    /**
     * To simplify result processing all results make use of Visitor Pattern.
     */
    void accept(ResultVisitor visitor);
}
