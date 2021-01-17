package com.zhouxiaoge.dag.cache;

import com.zhouxiaoge.dag.kafka.KafkaConsumerThread;
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
    public static final Map<String, List<Task>> DAG_TASKS_RELATION = new ConcurrentHashMap<>();

    public static void putDagTasksRelation(String dagKey, List<Task> taskList) {
        DAG_TASKS_RELATION.put(dagKey, taskList);
    }

    public static List<Task> getDagTasksRelation(String dagKey) {
        return DAG_TASKS_RELATION.get(dagKey);
    }

    public static void removeDagTasksRelation(String dagKey) {
        DAG_TASKS_RELATION.remove(dagKey);
    }

    public static Map<String, List<Task>> getDagAll() {
        return DAG_TASKS_RELATION;
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

    public static void removeDagDefaultTaskSubmitter(String dagKey) {
        DAG_DEFAULT_TASK_SUBMITTER.remove(dagKey);
    }

    /**
     * DAG启动的Kafka消费者集合
     */
    public static final Map<String, List<KafkaConsumerThread>> DAG_KAFKA_CONSUMER_THREAD = new ConcurrentHashMap<>();

    public static void putKafkaConsumerThreadList(String dagKey, List<KafkaConsumerThread> kafkaConsumerThreadList) {
        DAG_KAFKA_CONSUMER_THREAD.put(dagKey, kafkaConsumerThreadList);
    }

    public static List<KafkaConsumerThread> getKafkaConsumerThreadList(String dagKey) {
        return DAG_KAFKA_CONSUMER_THREAD.get(dagKey);
    }

    public static void removeKafkaConsumerThreadList(String dagKey) {
        DAG_KAFKA_CONSUMER_THREAD.remove(dagKey);
    }

}
