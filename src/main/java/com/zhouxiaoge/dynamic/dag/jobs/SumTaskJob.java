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
        Object key = contextData.get("key");
        Integer i = Integer.valueOf(key.toString());
        if (i > 3) {
            return Promise.ofCause(new RuntimeException("just failed"));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("key", i + 1);
        System.out.println("SumTaskJob-->" + resultMap);
        return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), null, resultMap));
    }
}
