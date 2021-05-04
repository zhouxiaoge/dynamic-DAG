package com.zhouxiaoge.dag.exec;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.constant.SysConstant;
import com.zhouxiaoge.dag.jobs.PrintTaskJob;
import com.zhouxiaoge.dag.jobs.RDBMSTaskJob;
import com.zhouxiaoge.dag.jobs.SumTaskJob;
import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.BatchExecution;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.impl.DefaultTask;
import com.zhouxiaoge.dag.node.Node;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import lombok.extern.slf4j.Slf4j;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.zhouxiaoge.dag.constant.SysConstant.*;

/**
 * @author 周小哥
 */

@Slf4j
@Service
public class DagExecutor {

    public Map<String, Object> synchronizationExecTask(String dagKey, Map<String, Object> parameterMap) {
        Map<String, Object> result = new HashMap<>(1);
        String batchId = dagKey + "-" + IdUtil.fastSimpleUUID();
        try {
            DefaultTaskSubmitter submitter = DagCacheUtils.getDagDefaultTaskSubmitter(dagKey);
            List<Task> list = DagCacheUtils.getDagTasksRelation(dagKey);
            List<Task> newTaskList = ObjectUtil.cloneByStream(list);

            Task[] tasks = newTaskList.stream().peek(task -> task.getTaskData().putAll(parameterMap)).toArray(Task[]::new);
            Batch<Task> taskBatch = Batch.of(batchId, tasks);
            Promise<TaskResult, Throwable> taskResultThrowablePromise = submitter.submitTasks(taskBatch);
            TaskResult taskResult = taskResultThrowablePromise.get(1, TimeUnit.MINUTES);
            boolean successful = taskResult.isSuccessful();
            result.put(RESULT, successful ? EXEC_RESULT_SUCCESS : EXEC_RESULT_FAIL);
        } catch (PromiseException | InterruptedException | TimeoutException e) {
            log.error("处理数据" + parameterMap.toString() + "失败，失败原因:", e);
            result.put(RESULT, EXEC_RESULT_FAIL);
            result.put(SysConstant.ERROR_MSG, e.getMessage());
        } finally {
            MemBasedTaskStorage memBasedTaskStorage = DagCacheUtils.getMemBasedTaskStorage(dagKey);
            if (null != memBasedTaskStorage) {
                Map<String, BatchExecution> executionMap = memBasedTaskStorage.getExecutionMap();
                executionMap.remove(batchId);
            }
        }
        return result;
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
        DagCacheUtils.putMemBasedTaskStorage(dagKey, taskStorage);
        HashedTaskRouter taskRouter = new HashedTaskRouter(4);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);
        DagCacheUtils.putDagDefaultTaskSubmitter(dagKey, submitter);
    }
}
