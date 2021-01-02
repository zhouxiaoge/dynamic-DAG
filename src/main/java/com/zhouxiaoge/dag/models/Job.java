package com.zhouxiaoge.dag.models;

import org.joo.promise4j.Promise;

import java.io.Serializable;

public interface Job extends Serializable {

    TaskTopo getTaskTopo();

    Promise<TaskResult, Exception> run(ExecutionContext context);
}
