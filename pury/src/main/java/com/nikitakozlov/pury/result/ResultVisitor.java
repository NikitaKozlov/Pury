package com.nikitakozlov.pury.result;

import com.nikitakozlov.pury.result.model.AverageProfileResult;
import com.nikitakozlov.pury.result.model.RootAverageProfileResult;
import com.nikitakozlov.pury.result.model.RootSingleProfileResult;
import com.nikitakozlov.pury.result.model.SingleProfileResult;

public interface ResultVisitor {
    void visit(AverageProfileResult averageProfileResult);

    void visit(RootAverageProfileResult rootAverageProfileResult);

    void visit(RootSingleProfileResult rootSingleProfileResult);

    void visit(SingleProfileResult singleProfileResult);
}
