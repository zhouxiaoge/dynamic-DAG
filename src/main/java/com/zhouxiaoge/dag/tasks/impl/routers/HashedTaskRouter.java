package com.zhouxiaoge.dag.tasks.impl.routers;

import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.tasks.TaskNotifier;
import com.zhouxiaoge.dag.tasks.TaskRouter;
import org.joo.promise4j.Promise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashedTaskRouter implements TaskRouter {

    private static final long serialVersionUID = 3542951723310626472L;

    private ExecutorService[] routers;

    public HashedTaskRouter(int routerThreads) {
        this.routers = new ExecutorService[routerThreads];
        for (int i = 0; i < routerThreads; i++) {
            routers[i] = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public Promise<Object, Throwable> routeJob(TaskNotifier notifier, String routingKey, Job job, TaskResult result) {
        ExecutorService router = findRouter(routingKey);
        String taskId = job.getTaskTopo().getTaskId();
        router.submit(() -> notifier.notifyJobComplete(routingKey, taskId, result));
        return Promise.of(null);
    }


    @Override
    public Promise<Object, Throwable> routeBatch(TaskNotifier notifier, String batchId) {
        ExecutorService router = findRouter(batchId);
        router.submit(() -> notifier.notifyBatchStart(batchId));
        return Promise.of(null);
    }

    protected ExecutorService findRouter(String routingKey) {
        int hash = routingKey.hashCode();
        return this.routers[Math.abs(hash % this.routers.length)];
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
