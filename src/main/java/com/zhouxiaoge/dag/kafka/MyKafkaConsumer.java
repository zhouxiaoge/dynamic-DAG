package com.zhouxiaoge.dag.kafka;

import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.exec.DagExecutor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 周小哥
 * @date 2021年01月11日 18点09分
 */

@Component
public class MyKafkaConsumer {

    private static final int KAFKA_THREAD_NUM = 3;

    private final DagExecutor dagExecutor;

    public MyKafkaConsumer(DagExecutor dagExecutor) {
        this.dagExecutor = dagExecutor;
    }

    public void consumer(String dagKey) {
        List<KafkaConsumerThread> kafkaConsumerThreadList = new ArrayList<>();
        for (int i = 0; i < KAFKA_THREAD_NUM; i++) {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaUtils.BOOTSTRAP_SERVERS);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "GROUP_ID");
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
            props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
            KafkaConsumerThread kafkaConsumerThread = new KafkaConsumerThread("DAG-Thread-" + i, consumer, KafkaUtils.KAFKA_TOPIC);
            kafkaConsumerThread.setDagKey(dagKey);
            kafkaConsumerThread.setExecService(dagExecutor);
            kafkaConsumerThread.start();
            kafkaConsumerThreadList.add(kafkaConsumerThread);
        }
        DagCacheUtils.putKafkaConsumerThreadList(dagKey, kafkaConsumerThreadList);
    }
}