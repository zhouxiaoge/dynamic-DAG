package com.zhouxiaoge.dag.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouxiaoge.dag.service.ExecService;
import kafka.utils.ShutdownableThread;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * @author 周小哥
 * @date 2021年01月11日 18点09分
 */

public class KafkaConsumerThread extends ShutdownableThread {

    public static boolean isRunning = true;
    private final KafkaConsumer<String, String> kafkaConsumer;

    private ExecService execService;

    private String dagKey;

    public void setDagKey(String dagKey) {
        this.dagKey = dagKey;
    }

    public void setExecService(ExecService execService) {
        this.execService = execService;
    }

    public KafkaConsumerThread(String name, KafkaConsumer<String, String> kafkaConsumer, String topic) {
        super(name, true);
        this.kafkaConsumer = kafkaConsumer;
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    @SneakyThrows
    @Override
    public void doWork() {
        while (isRunning) {
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> stringStringConsumerRecord : poll) {
                String value = stringStringConsumerRecord.value();
                ObjectMapper objectMapper = new ObjectMapper();
                Map map = objectMapper.readValue(value, Map.class);
                map.put("THREAD_NAME", Thread.currentThread().getName());
                this.execService.asynExecTask(this.dagKey, map);
            }
        }
    }
}