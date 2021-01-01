package com.zhouxiaoge.dynamic.dag.jobs;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import com.zhouxiaoge.dynamic.dag.models.impl.results.DefaultTaskResult;
import io.gridgo.bean.BObject;
import org.joo.promise4j.Promise;

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
        return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), BObject.of("SumTask", context.getBatchId())));
    }
}