package com.zhouxiaoge.dynamic.dag.models.impl.results;

import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskResultStatus;
import lombok.Getter;

import java.util.Map;

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
        boolean flag = true;
        for (String s : result.keySet()) {
            TaskResult taskResult = result.get(s);
            boolean successful = taskResult.isSuccessful();
            if (!successful) {
                flag = false;
                break;
            }
        }
        successful = flag;
        if (!successful) {
            status = TaskResultStatus.FAILED;
        } else {
            status = TaskResultStatus.FINISHED;
        }
    }
}
