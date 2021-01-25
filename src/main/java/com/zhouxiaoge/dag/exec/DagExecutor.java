package com.zhouxiaoge.dag.exec;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.jobs.PrintTaskJob;
import com.zhouxiaoge.dag.jobs.RDBMSTaskJob;
import com.zhouxiaoge.dag.jobs.SumTaskJob;
import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.impl.DefaultTask;
import com.zhouxiaoge.dag.node.Node;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周小哥
 */
@Service
public class DagExecutor {

    public boolean asynExecTask(String dagKey, Map<String, Object> parameterMap) throws InterruptedException, PromiseException {
        DefaultTaskSubmitter submitter = DagCacheUtils.getDagDefaultTaskSubmitter(dagKey);
        submitter.start();
        List<Task> list = DagCacheUtils.getDagTasksRelation(dagKey);
        List<Task> newTaskList = ObjectUtil.cloneByStream(list);

        Task[] tasks = newTaskList.stream().peek(task -> task.getTaskData().putAll(parameterMap)).toArray(Task[]::new);
        Batch<Task> taskBatch = Batch.of(dagKey + "-" + IdUtil.fastSimpleUUID(), tasks);
        Promise<TaskResult, Throwable> taskResultThrowablePromise = submitter.submitTasks(taskBatch);
        TaskResult taskResult = taskResultThrowablePromise.get();
        submitter.stop();
        return taskResult.isSuccessful();
    }

    public void generateTaskDependant(String dagKey) {
        List<Task> list = new ArrayList<>();
        Node node = new Node();
        Map<String, Object> map = new HashMap<>();
        map.put("variable", "variable—value");

        node.setNodeData(map);

        Task task1 = new DefaultTask("1", "task1", "print-task-job", new String[]{"2"}, new HashMap<>(16), node);
        Task task2 = new DefaultTask("2", "task2", "sum-task-job", new String[]{"3"}, new HashMap<>(16), node);
        Task task3 = new DefaultTask("3", "task5", "rdbms-task-job", new String[0], new HashMap<>(16), node);
        list.add(task1);
        list.add(task2);
        list.add(task3);
        DagCacheUtils.putDagTasksRelation(StrUtil.isBlankIfStr(dagKey) ? "dagKey" : dagKey, list);

        DefaultTaskMapper taskMapper = new DefaultTaskMapper()
                .with("print-task-job", PrintTaskJob::new)
                .with("sum-task-job", SumTaskJob::new)
                .with("rdbms-task-job", RDBMSTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);
        DagCacheUtils.putDagDefaultTaskSubmitter(dagKey, submitter);
    }
}
