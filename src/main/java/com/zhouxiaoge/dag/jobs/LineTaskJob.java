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
 * @date 2021年01月25日 22点42分
 */
@Slf4j
public class LineTaskJob implements Job {

    private static final long serialVersionUID = 1884714051217724807L;

    private TaskTopo taskTopo;

    public LineTaskJob(TaskTopo taskTopo) {
        this.taskTopo = taskTopo;
    }

    @Override
    public TaskTopo getTaskTopo() {
        return taskTopo;
    }

    @Override
    public Promise<TaskResult, Exception> run(ExecutionContext context) {
        log.debug("------------------------------LineTaskJob-{}-------------------------------------", taskTopo.getTaskId());
        try {
            Map<String, Object> contextData = context.getContextData();
            System.out.println("LineTaskJob-->" + context.getBatchId() + ":" + contextData);
            return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), null, contextData));
        } catch (Exception e) {
            log.error("执行异常", e);
            return Promise.ofCause(new RuntimeException("PrintTaskJob异常"));
        }
    }
}
