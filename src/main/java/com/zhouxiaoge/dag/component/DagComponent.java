package com.zhouxiaoge.dag.component;

import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.exec.DagExecutor;
import com.zhouxiaoge.dag.models.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 周小哥
 * @date 2021年01月11日 21点36分
 */
@Component
public class DagComponent {

    private final DagExecutor dagExecutor;

    public DagComponent(DagExecutor dagExecutor) {
        this.dagExecutor = dagExecutor;
    }

    public void startDag(String dagKey) {
        dagExecutor.generateTaskDependant(dagKey);
    }

    public void stopDag(String dagKey) {
        DagCacheUtils.removeDagDefaultTaskSubmitter(dagKey);
        DagCacheUtils.removeDagTasksRelation(dagKey);
    }

    public Set<String> getDagAll() {
        Map<String, List<Task>> dagAll = DagCacheUtils.getDagAll();
        return dagAll.keySet();
    }
}
