package com.zhouxiaoge.dynamic.dag.models;

import java.io.Serializable;

import org.joo.promise4j.Promise;

public interface Job extends Serializable {

    TaskTopo getTaskTopo();

    Promise<TaskResult, Exception> run(ExecutionContext context);
}
