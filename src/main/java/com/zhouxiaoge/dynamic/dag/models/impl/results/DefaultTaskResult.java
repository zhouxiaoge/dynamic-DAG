package com.zhouxiaoge.dynamic.dag.models.impl.results;

import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskResultStatus;

import java.util.Map;

public class DefaultTaskResult implements TaskResult {

    private String id;

    private Map<String, TaskResult> result;

    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public DefaultTaskResult(String id, Map<String, TaskResult> result, Map<String, Object> data) {
        this.id = id;
        this.result = result;
        this.data = data;
    }

    public DefaultTaskResult(String id, Map<String, TaskResult> result) {
        this.id = id;
        this.result = result;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, TaskResult> getResult() {
        return result;
    }

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
