package com.zhouxiaoge.dynamic.dag.test.jobs;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import org.joo.promise4j.Promise;

import lombok.Getter;

@Getter
public class FailTaskJob implements Job {

    private static final long serialVersionUID = -4403082105580281768L;

    private TaskTopo taskTopo;

    public FailTaskJob(TaskTopo taskTopo) {
        this.taskTopo = taskTopo;
    }

    @Override
    public Promise<TaskResult, Exception> run(ExecutionContext context) {
        if (Math.random() > 0.5) {
            return Promise.ofCause(new RuntimeException("just failed"));
        }
        return Promise.of(null);
    }
}
