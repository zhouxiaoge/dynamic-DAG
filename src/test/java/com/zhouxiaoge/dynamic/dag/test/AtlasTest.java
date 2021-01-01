package com.zhouxiaoge.dynamic.dag.test;

import com.zhouxiaoge.dynamic.dag.jobs.PrintTaskJob;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.impl.results.BatchTaskResult;
import com.zhouxiaoge.dynamic.dag.support.exceptions.CyclicGraphDetectedException;
import com.zhouxiaoge.dynamic.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dynamic.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dynamic.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.storages.MemBasedTaskStorage;
import com.zhouxiaoge.dynamic.dag.test.jobs.FailTaskJob;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AtlasTest extends AtlasBaseTest {

    @Test
    public void testCircularDependency() throws PromiseException, InterruptedException {
        var taskMapper = new DefaultTaskMapper().with("test-task", PrintTaskJob::new);
        var taskStorage = new MemBasedTaskStorage();
        var taskRouter = new HashedTaskRouter(2);
        var taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        var submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        var batch = createBatchWithCircularDependency();

        try {
            submitter.submitTasks(batch).get();
            Assert.fail("must fail with cyclic graph detected exception");
        } catch (PromiseException ex) {
            if (!(ex.getCause() instanceof CyclicGraphDetectedException)) {
                Assert.fail(ex.getCause().getMessage());
            }
        }

        submitter.stop();
    }

    @Test
    public void testRandomFailure() throws InterruptedException, PromiseException {
        var taskMapper = new DefaultTaskMapper().with("test-task", FailTaskJob::new);
        var taskStorage = new MemBasedTaskStorage();
        var taskRouter = new HashedTaskRouter(2);
        var taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        var submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        var promises = new ArrayList<Promise<TaskResult, Throwable>>();
        for (var i = 0; i < 10; i++) {
            var batch = createBatch("test" + i);
            var promise = submitter.submitTasks(batch);
            promises.add(promise);
        }

        var results = Promise.all(promises).get();
        for (var result : results) {
            var batchResult = (BatchTaskResult) result;
            System.out.println(batchResult.getResult());
        }

        submitter.stop();
    }

    @Test
    public void testGraph() throws InterruptedException, PromiseException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("test-task", PrintTaskJob::new);
        var taskStorage = new MemBasedTaskStorage();
        var taskRouter = new HashedTaskRouter(2);
        var taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        var submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        var promises = new ArrayList<Promise<TaskResult, Throwable>>();
        for (var i = 0; i < 5; i++) {
            var batch = createBatch("test" + i);
            var promise = submitter.submitTasks(batch);
            promises.add(promise);
        }

        Promise.all(promises).get();

        submitter.stop();
    }
}
