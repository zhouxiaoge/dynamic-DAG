package com.zhouxiaoge.dag.models.impl.results;

import com.zhouxiaoge.dag.exceptions.BatchException;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskResultStatus;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class BatchTaskResult implements TaskResult {

    private String id;

    private boolean successful;

    private Throwable cause;

    private Map<String, TaskResult> result;

    private TaskResultStatus status;

    public BatchTaskResult(String id, Map<String, TaskResult> results) {
        this.id = id;
        this.result = results;
        checkForFailure();
    }

    private void checkForFailure() {

        Map<String, Optional<Throwable>> failures = new HashMap<>(16);
        for (String s : result.keySet()) {
            TaskResult taskResult = result.get(s);
            boolean successful = taskResult.isSuccessful();
            if (!successful) {
                failures.put(taskResult.getId(), Optional.ofNullable(taskResult.getCause()));
            }
        }
        successful = failures.isEmpty();
        if (!successful) {
            cause = new BatchException(failures);
            status = TaskResultStatus.FAILED;
        } else {
            status = TaskResultStatus.FINISHED;
        }
    }
}
