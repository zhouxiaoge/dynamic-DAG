package com.zhouxiaoge.dag.kafka;

import com.zhouxiaoge.dag.service.ExecService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author 周小哥
 * @date 2021年01月11日 18点09分
 */

@Component
public class DagKafkaConsumer {

    private static final int KAFKA_THREAD_NUM = 3;

    private final ExecService execService;

    public DagKafkaConsumer(ExecService execService) {
        this.execService = execService;
    }

    public void consumer(String dagKey) {
        for (int i = 0; i < KAFKA_THREAD_NUM; i++) {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaUtils.BOOTSTRAP_SERVERS);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "GROUP_ID");
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
            KafkaConsumerThread kafkaConsumerThread = new KafkaConsumerThread("Thread-" + i, consumer, KafkaUtils.KAFKA_TOPIC);
            kafkaConsumerThread.setDagKey(dagKey);
            kafkaConsumerThread.setExecService(execService);
            kafkaConsumerThread.start();
        }
    }
}