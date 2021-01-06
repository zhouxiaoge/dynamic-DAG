package com.zhouxiaoge.dag.test;

import com.zhouxiaoge.dag.models.ExecutionContext;
import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskTopo;
import lombok.Getter;
import org.joo.promise4j.Promise;

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
