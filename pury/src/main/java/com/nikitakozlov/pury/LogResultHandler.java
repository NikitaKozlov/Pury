package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.ResultVisitor;
import com.nikitakozlov.pury.result.model.AverageProfileResult;
import com.nikitakozlov.pury.result.model.ProfileResult;
import com.nikitakozlov.pury.result.model.RootAverageProfileResult;
import com.nikitakozlov.pury.result.model.RootSingleProfileResult;
import com.nikitakozlov.pury.result.model.SingleProfileResult;

import static com.nikitakozlov.pury.Pury.LOG_TAG;

public class LogResultHandler implements ResultHandler {

    private static final String RIGHT_ARROW = " --> ";
    private static final String LEFT_ARROW = " <-- ";
    private static final String MS = "ms";
    private static final String EXEC_TIME = "execution = ";
    private static final int MS_TO_NS = 1000000;
    private static final String DEPTH_PREFIX = "  ";

    @Override
    public void handleResult(ProfileResult result, ProfilerId profilerId) {
        Visitor visitor = new Visitor();
        result.accept(visitor);

        StringBuilder logMessage = new StringBuilder("Profiling results");
        String methodId = profilerId.getProfilerName();
        if (!methodId.isEmpty()) {
            logMessage.append(" for ");
            logMessage.append(methodId);
        }
        logMessage.append(":\n");
        logMessage.append(visitor.getLogMessage());
        Pury.getLogger().result(LOG_TAG, logMessage.toString());
    }


    private static class Visitor implements ResultVisitor {

        private final StringBuilder logMessageBuilder = new StringBuilder();

        @Override
        public void visit(AverageProfileResult averageProfileResult) {

            appendFullDepthPrefix(logMessageBuilder, averageProfileResult.getDepth());
            logMessageBuilder.append(averageProfileResult.getStageName());
            logMessageBuilder.append(RIGHT_ARROW);
            logMessageBuilder.append(averageProfileResult.getStartTime());
            logMessageBuilder.append("\n");
            for (ProfileResult result : averageProfileResult.getNestedResults()) {
                result.accept(this);
                logMessageBuilder.append("\n");
            }
            appendFullDepthPrefix(logMessageBuilder, averageProfileResult.getDepth());
            logMessageBuilder.append(averageProfileResult.getStageName());
            logMessageBuilder.append(LEFT_ARROW);
            logMessageBuilder.append(averageProfileResult.getExecTime());
        }

        @Override
        public void visit(RootAverageProfileResult rootAverageProfileResult) {
            logMessageBuilder.append(rootAverageProfileResult.getStageName());
            logMessageBuilder.append(" --> 0ms\n");
            for (ProfileResult result : rootAverageProfileResult.getNestedResults()) {
                result.accept(this);
                logMessageBuilder.append("\n");
            }
            logMessageBuilder.append(rootAverageProfileResult.getStageName());
            logMessageBuilder.append(LEFT_ARROW);
            logMessageBuilder.append(rootAverageProfileResult.getExecTime());
        }

        @Override
        public void visit(RootSingleProfileResult rootSingleProfileResult) {

            logMessageBuilder.append(rootSingleProfileResult.getStageName());
            logMessageBuilder.append(" --> 0ms\n");
            for (ProfileResult result : rootSingleProfileResult.getNestedResults()) {
                result.accept(this);
                logMessageBuilder.append("\n");
            }
            logMessageBuilder.append(rootSingleProfileResult.getStageName());
            logMessageBuilder.append(LEFT_ARROW);
            logMessageBuilder.append(rootSingleProfileResult.getExecTime() / MS_TO_NS);
            logMessageBuilder.append(MS);
        }

        @Override
        public void visit(SingleProfileResult singleProfileResult) {

            appendFullDepthPrefix(logMessageBuilder, singleProfileResult.getDepth());
            logMessageBuilder.append(singleProfileResult.getStageName());
            logMessageBuilder.append(RIGHT_ARROW);
            logMessageBuilder.append(singleProfileResult.getStartTime() / MS_TO_NS);
            logMessageBuilder.append("ms\n");
            for (ProfileResult result : singleProfileResult.getNestedResults()) {
                result.accept(this);
                logMessageBuilder.append("\n");
            }
            appendFullDepthPrefix(logMessageBuilder, singleProfileResult.getDepth());
            logMessageBuilder.append(singleProfileResult.getStageName());
            logMessageBuilder.append(LEFT_ARROW);
            logMessageBuilder.append((singleProfileResult.getStartTime() + singleProfileResult.getExecTime()) / MS_TO_NS);
            logMessageBuilder.append("ms, ");
            logMessageBuilder.append(EXEC_TIME);
            logMessageBuilder.append(singleProfileResult.getExecTime() / MS_TO_NS);
            logMessageBuilder.append(MS);
        }

        String getLogMessage() {
            return logMessageBuilder.toString();
        }

        private void appendFullDepthPrefix(StringBuilder sb, int depth) {
            for (int i = 0; i < depth; i++) {
                sb.append(DEPTH_PREFIX);
            }
        }
    }
}
