package com.zhouxiaoge.dynamic.dag.tasks.impl.queue;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;

import lombok.Data;

@Data
public class HazelcastCallableJob implements Serializable, Callable<TaskResult> {

    private static final long serialVersionUID = -2552737194281028688L;

    private Job job;

    private ExecutionContext context;

    @Override
    public TaskResult call() throws Exception {
        return job.run(context).get();
    }
}
