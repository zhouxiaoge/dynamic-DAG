package com.zhouxiaoge.dynamic.dag.tasks.impl.queue;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.tasks.TaskRouter;
import com.zhouxiaoge.dynamic.dag.tasks.TaskStorage;
import org.joo.promise4j.Promise;
import org.joo.promise4j.impl.CompletableDeferredObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class PooledTaskRunner extends AbstractTaskQueue {

    private ExecutorService pool;

    public PooledTaskRunner(int workerThreads, TaskRouter router, TaskStorage storage) {
        super(router, storage);
        this.pool = new ForkJoinPool(workerThreads);
    }

    @Override
    protected Promise<TaskResult, Exception> doRunJob(Job job, ExecutionContext context) {
        var deferred = new CompletableDeferredObject<TaskResult, Exception>();
        pool.submit(() -> {
            job.run(context).forward(deferred);
        });
        return deferred.promise();
    }

    @Override
    public void onStop() {
        super.onStop();
        pool.shutdownNow();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
