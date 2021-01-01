package com.zhouxiaoge.dynamic.dag.models;

import java.util.Map;

public interface TaskResult {

    String getId();

    TaskResultStatus getStatus();

    boolean isSuccessful();

    Throwable getCause();

    Map<String, TaskResult> getResult();

}
