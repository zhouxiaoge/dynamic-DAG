package com.zhouxiaoge.dynamic.dag.tasks.impl.queue;

import java.io.Serializable;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.tasks.TaskRouter;

import lombok.Data;

@Data
public class HazelcastRunnableJob implements Serializable, Runnable {

    private static final long serialVersionUID = -2552737194281028688L;

    private Job job;

    private ExecutionContext context;
    
    private TaskRouter router;

    @Override
    public void run() {
        
    }
}