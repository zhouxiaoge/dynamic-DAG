package com.zhouxiaoge.dynamic.dag.tasks.impl.queue;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.tasks.TaskRouter;
import com.zhouxiaoge.dynamic.dag.tasks.TaskStorage;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class NonBlockingHazelcastTaskRunner extends AbstractTaskQueue {

    private HazelcastInstance hzc;

    private IExecutorService pool;

    public NonBlockingHazelcastTaskRunner(String serviceName, TaskRouter router, TaskStorage storage) {
        super(router, storage);
        this.hzc = Hazelcast.newHazelcastInstance();
        this.pool = hzc.getExecutorService(serviceName);
    }

    @Override
    protected void runJob(Job job, ExecutionContext context) {
        var hzcJob = new HazelcastRunnableJob();
        hzcJob.setContext(context);
        hzcJob.setJob(job);
        hzcJob.setRouter(getRouter());
        
        pool.submit(hzcJob);
    }

    @Override
    public void onStop() {
        super.onStop();
        pool.shutdownNow();
    }

    @Override
    protected void onStart() {
        // Nothing to do here
    }
}
