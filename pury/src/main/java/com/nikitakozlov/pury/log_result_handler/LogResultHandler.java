package com.nikitakozlov.pury.log_result_handler;

import android.util.Log;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.ResultHandler;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.ResultVisitor;
import com.nikitakozlov.pury.result.model.AverageProfileResult;
import com.nikitakozlov.pury.result.model.ProfileResult;
import com.nikitakozlov.pury.result.model.RootAverageProfileResult;
import com.nikitakozlov.pury.result.model.RootSingleProfileResult;
import com.nikitakozlov.pury.result.model.SingleProfileResult;

public class LogResultHandler implements ResultHandler {

    @Override
    public void handleResult(ProfileResult result, ProfilerId profilerId) {
        Visitor visitor = new Visitor();
        result.accept(visitor);
        Pury.getLogger().result("Pury", visitor.getLogMessage());
    }

//    StringBuilder result = new StringBuilder("Profiling results");
//    String methodId = mProfilerId.getProfilerName();
//    if (!methodId.isEmpty()) {
//        result.append(" for ");
//        result.append(methodId);
//    }
//    result.append(":\n");
//    result.append(profileResult);
//    mLogger.result(LOG_TAG, result.toString());

    private static class Visitor implements ResultVisitor {

        private final StringBuilder logMessageBuilder = new StringBuilder();

        @Override
        public void visit(AverageProfileResult averageProfileResult) {
            Log.d("Result", "AverageProfileResult");
        }

        @Override
        public void visit(RootAverageProfileResult rootAverageProfileResult) {
            Log.d("Result", "RootAverageProfileResult");
        }

        @Override
        public void visit(RootSingleProfileResult rootSingleProfileResult) {
            Log.d("Result", "RootSingleProfileResult");
        }

        @Override
        public void visit(SingleProfileResult singleProfileResult) {
            Log.d("Result", "SingleProfileResult");
        }

        String getLogMessage() {
            return logMessageBuilder.toString();
        }
    }
}
