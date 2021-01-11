package com.zhouxiaoge.dag.kafka;

import com.zhouxiaoge.dag.service.ExecService;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * @author 周小哥
 * @date 2021年01月11日 18点09分
 */

@Component
public class DagKafkaConsumer {

    private final ExecService execService;

    public DagKafkaConsumer(ExecService execService) {
        this.execService = execService;
    }

    public void consumer(String dagKey) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.124.13:9092");
        props.put("group.id", "GROUP_ID");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor("zmy");
        for (PartitionInfo partitionInfo : partitionInfos) {
            KafkaConsumerThread kafkaConsumerThread = new KafkaConsumerThread(partitionInfo.toString(), consumer, "zmy");
            kafkaConsumerThread.setDagKey(dagKey);
            kafkaConsumerThread.setExecService(execService);
            kafkaConsumerThread.start();
        }
    }
}