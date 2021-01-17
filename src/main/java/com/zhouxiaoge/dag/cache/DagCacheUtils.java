package com.zhouxiaoge.dag.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;
import com.zhouxiaoge.dag.models.Task;

import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月17日 12点46分
 */
public class DagCacheUtils {

    public static final LFUCache<String, List<Task>> DAG_TASKS_RELATION = CacheUtil.newLFUCache(200);

    public static void putDagTasksRelation(String dagKey, List<Task> taskList) {
        DAG_TASKS_RELATION.put(dagKey, taskList);
    }

    public static List<Task> getDagTasksRelation(String dagKey) {
        return DAG_TASKS_RELATION.get(dagKey);
    }
}
