package com.zhouxiaoge.dynamic.dag.models;

import io.gridgo.bean.BObject;

import java.util.Map;

public interface TaskResult {

    String getId();

    TaskResultStatus getStatus();

    boolean isSuccessful();

    Throwable getCause();

    Map<String, TaskResult> getResult();

    default BObject toBObject() {
        return BObject.of("id", getId()) //
                .setAny("result", getResult()) //
                .setAny("status", getStatus().name()) //
                .setAny("cause", getCause() != null ? getCause().toString() : null);
    }
}
