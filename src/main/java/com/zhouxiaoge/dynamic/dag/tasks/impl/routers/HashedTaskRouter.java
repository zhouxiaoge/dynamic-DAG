package com.zhouxiaoge.dynamic.dag.tasks.impl.routers;

import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.tasks.TaskNotifier;
import com.zhouxiaoge.dynamic.dag.tasks.TaskRouter;
import io.gridgo.framework.impl.NonameComponentLifecycle;
import org.joo.promise4j.Promise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashedTaskRouter extends NonameComponentLifecycle implements TaskRouter {

    private static final long serialVersionUID = 3542951723310626472L;

    private ExecutorService[] routers;

    public HashedTaskRouter(int routerThreads) {
        this.routers = new ExecutorService[routerThreads];
        for (var i = 0; i < routerThreads; i++) {
            routers[i] = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public Promise<Object, Throwable> routeJob(TaskNotifier notifier, String routingKey, Job job, TaskResult result) {
        var router = findRouter(routingKey);
        var taskId = job.getTaskTopo().getTaskId();
        router.submit(() -> notifier.notifyJobComplete(routingKey, taskId, result));
        return Promise.of(null);
    }

    @Override
    protected void onStop() {
        for (var router : routers) {
            router.shutdownNow();
        }
    }

    @Override
    public Promise<Object, Throwable> routeBatch(TaskNotifier notifier, String batchId) {
        var router = findRouter(batchId);
        router.submit(() -> notifier.notifyBatchStart(batchId));
        return Promise.of(null);
    }

    protected ExecutorService findRouter(String routingKey) {
        var hash = routingKey.hashCode();
        return this.routers[Math.abs(hash % this.routers.length)];
    }

    @Override
    protected void onStart() {
        // Nothing to do here
    }
}
