package com.zhouxiaoge.dynamic.dag.jobs;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import com.zhouxiaoge.dynamic.dag.models.impl.results.DefaultTaskResult;
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
        Map<String, Object> contextData = context.getContextData();
        System.out.println("PrintTaskJob-->" + contextData);
        return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), null, contextData));
    }
}
