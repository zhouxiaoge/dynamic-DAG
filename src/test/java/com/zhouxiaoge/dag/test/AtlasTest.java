package com.zhouxiaoge.dag.test;

import com.zhouxiaoge.dag.exceptions.CyclicGraphDetectedException;
import com.zhouxiaoge.dag.jobs.PrintTaskJob;
import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.impl.results.BatchTaskResult;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.joo.promise4j.impl.JoinedResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class AtlasTest extends AtlasBaseTest {

    @Test
    public void testCircularDependency() throws InterruptedException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("test-task", PrintTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        Batch<Task> batch = createBatchWithCircularDependency();

        try {
            submitter.submitTasks(batch).get();
            Assertions.fail("must fail with cyclic graph detected exception");
        } catch (PromiseException ex) {
            if (!(ex.getCause() instanceof CyclicGraphDetectedException)) {
                Assertions.fail(ex.getCause().getMessage());
            }
        }

        submitter.stop();
    }

    @Test
    public void testRandomFailure() throws InterruptedException, PromiseException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("test-task", FailTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        List<Promise<TaskResult, Throwable>> promises = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Batch<Task> batch = createBatch("test" + i);
            Promise<TaskResult, Throwable> promise = submitter.submitTasks(batch);
            promises.add(promise);
        }

        JoinedResults<TaskResult> results = Promise.all(promises).get();
        for (TaskResult result : results) {
            BatchTaskResult batchResult = (BatchTaskResult) result;
            System.out.println(batchResult.getResult());
        }

        submitter.stop();
    }

    @Test
    public void testGraph() throws InterruptedException, PromiseException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("test-task", PrintTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        List<Promise<TaskResult, Throwable>> promises = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Batch<Task> batch = createBatch("test" + i);
            Promise<TaskResult, Throwable> promise = submitter.submitTasks(batch);
            promises.add(promise);
        }

        Promise.all(promises).get();

        submitter.stop();
    }
}
