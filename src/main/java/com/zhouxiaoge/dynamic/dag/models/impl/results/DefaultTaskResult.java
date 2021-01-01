package com.zhouxiaoge.dynamic.dag.models.impl.results;

import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskResultStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DefaultTaskResult implements TaskResult {

    private String id;

    private Map<String, TaskResult> result;

    public DefaultTaskResult() {

    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    @Override
    public TaskResultStatus getStatus() {
        return TaskResultStatus.FINISHED;
    }

    @Override
    public String toString() {
        return "Task completed sucessfully";
    }

    @Override
    public Throwable getCause() {
        return null;
    }
}
