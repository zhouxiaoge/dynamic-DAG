package com.zhouxiaoge.dag.jobs;

import com.zhouxiaoge.dag.models.ExecutionContext;
import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskTopo;
import com.zhouxiaoge.dag.models.impl.results.DefaultTaskResult;
import org.joo.promise4j.Promise;

import java.util.HashMap;
import java.util.Map;

public class PrintTaskJob implements Job {

    private static final long serialVersionUID = 6875856662528294631L;

    private TaskTopo taskTopo;

    public PrintTaskJob(TaskTopo taskTopo) {
        this.taskTopo = taskTopo;
    }

    @Override
    public TaskTopo getTaskTopo() {
        return taskTopo;
    }

    @Override
    public Promise<TaskResult, Exception> run(ExecutionContext context) {
        try {
            Map<String, Object> contextData = context.getContextData();
            Object key = contextData.get("key");
            int i = Integer.parseInt(key.toString());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", i * 10);
            System.out.println("PrintTaskJob-->" + context.getBatchId() + ":" + resultMap);
            return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), null, resultMap));
        } catch (Exception e) {
            return Promise.ofCause(new RuntimeException("PrintTaskJob异常"));
        }
    }
}
