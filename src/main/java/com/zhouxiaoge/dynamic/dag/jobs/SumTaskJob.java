package com.zhouxiaoge.dynamic.dag.jobs;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import com.zhouxiaoge.dynamic.dag.models.impl.results.DefaultTaskResult;
import org.joo.promise4j.Promise;

import java.util.HashMap;
import java.util.Map;

public class SumTaskJob implements Job {

    private static final long serialVersionUID = -3579110674551524656L;

    private TaskTopo taskTopo;

    @Override
    public TaskTopo getTaskTopo() {
        return taskTopo;
    }

    public SumTaskJob(TaskTopo taskTopo) {
        this.taskTopo = taskTopo;
    }

    @Override
    public Promise<TaskResult, Exception> run(ExecutionContext context) {
        Map<String, Object> contextData = context.getContextData();
        System.out.println("SumTaskJob-->" + contextData);
        Map<String, TaskResult> result = new HashMap<>();
        DefaultTaskResult taskResult = new DefaultTaskResult();
        Map<String, Object> map = new HashMap<>();
        map.put(taskTopo.getTaskId(), context.getBatchId() + "-" + Math.random());
        taskResult.setData(contextData);
        result.put("SumTask", taskResult);
        return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), result));
    }
}
