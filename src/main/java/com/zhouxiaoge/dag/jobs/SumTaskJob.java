package com.zhouxiaoge.dag.jobs;

import com.zhouxiaoge.dag.models.ExecutionContext;
import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskTopo;
import com.zhouxiaoge.dag.models.impl.results.DefaultTaskResult;
import lombok.extern.slf4j.Slf4j;
import org.joo.promise4j.Promise;

import java.util.Map;

/**
 * @author 周小哥
 */
@Slf4j
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
        try {
            Map<String, Object> contextData = context.getContextData();
            for (String s : contextData.keySet()) {
                if ("SEX".equalsIgnoreCase(s)) {
                    Object o = contextData.get(s);
                    if ("0".equals(String.valueOf(o))) {
                        contextData.put("SEX", "女");
                    } else {
                        contextData.put("SEX", "男");
                    }
                }
            }
            return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), null, contextData));
        } catch (Exception e) {
            log.error("执行异常", e);
            return Promise.ofCause(new RuntimeException("just failed"));
        }
    }
}
