package com.zhouxiaoge.dag.models.impl.results;

import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskResultStatus;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
public class FailedTaskResult implements TaskResult {

    private String id;

    private Throwable cause;

    public FailedTaskResult(String id, @NonNull Throwable cause) {
        this.id = id;
        this.cause = cause;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public TaskResultStatus getStatus() {
        return TaskResultStatus.FAILED;
    }

    @Override
    public Map<String, TaskResult> getResult() {
        return null;
    }

    @Override
    public String toString() {
        return "Task failed with exception [" + cause + "]";
    }
}
