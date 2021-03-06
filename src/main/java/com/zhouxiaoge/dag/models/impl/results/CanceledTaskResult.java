package com.zhouxiaoge.dag.models.impl.results;

import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskResultStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class CanceledTaskResult implements TaskResult {

    private String id;

    public CanceledTaskResult(String id) {
        this.id = id;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public TaskResultStatus getStatus() {
        return TaskResultStatus.CANCELED;
    }

    @Override
    public Throwable getCause() {
        return null;
    }

    @Override
    public Map<String, TaskResult> getResult() {
        return null;
    }

    @Override
    public String toString() {
        return "Task canceled";
    }
}
