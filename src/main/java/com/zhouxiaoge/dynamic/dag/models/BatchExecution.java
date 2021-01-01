package com.zhouxiaoge.dynamic.dag.models;

import org.joo.promise4j.Deferred;

import java.util.Map;

public interface BatchExecution {

    Deferred<TaskResult, Throwable> getDeferred();

    Batch<Job> getBatch();
    
    Map<String, TaskResult> getCompletedJobs();
    
    Job mapTask(String taskId);

    void completeJob(Job job, TaskResult result);

    boolean canRun(Job job);

    boolean isCompleted();
}
