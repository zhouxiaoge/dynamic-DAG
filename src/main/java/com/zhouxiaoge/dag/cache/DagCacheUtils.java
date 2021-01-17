package com.zhouxiaoge.dag.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskSubmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周小哥
 * @date 2021年01月17日 12点46分
 */
public class DagCacheUtils {

    /**
     * DAG关系图缓存
     */
    public static final LFUCache<String, List<Task>> DAG_TASKS_RELATION = CacheUtil.newLFUCache(200);

    public static void putDagTasksRelation(String dagKey, List<Task> taskList) {
        DAG_TASKS_RELATION.put(dagKey, taskList);
    }

    public static List<Task> getDagTasksRelation(String dagKey) {
        return DAG_TASKS_RELATION.get(dagKey);
    }

    /**
     * DAG对象PooledTaskRunner缓存
     */
    public static final Map<String, DefaultTaskSubmitter> DAG_DEFAULT_TASK_SUBMITTER = new ConcurrentHashMap<>();

    public static void putDagDefaultTaskSubmitter(String dagKey, DefaultTaskSubmitter defaultTaskSubmitter) {
        DAG_DEFAULT_TASK_SUBMITTER.put(dagKey, defaultTaskSubmitter);
    }

    public static DefaultTaskSubmitter getDagDefaultTaskSubmitter(String dagKey) {
        return DAG_DEFAULT_TASK_SUBMITTER.get(dagKey);
    }

}
