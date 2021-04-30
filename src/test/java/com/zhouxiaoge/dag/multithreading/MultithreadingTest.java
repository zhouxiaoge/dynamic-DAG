package com.zhouxiaoge.dag.multithreading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouxiaoge.dag.exec.DagExecutor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
public class MultithreadingTest {

    @Autowired
    private DagExecutor dagExecutor;

    @Test
    public void multithreadingTest() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.124.13:9092");
        props.put("group.id", "GROUP_ID");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("zmy"));
        dagExecutor.generateTaskDependant("dagKey");
        System.out.println("------------------------------Kafka启动拉取数据-----------------------------------");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("-------------------------" + Thread.currentThread().getName());
                String value = record.value();
                System.out.println(value);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Thread.sleep(1000);
                    Map map = objectMapper.readValue(value, Map.class);
                    map.put("THREAD_NAME", Thread.currentThread().getName());
                    dagExecutor.asynchronizationExecTask("zhouxiaoge", map);

                } catch (JsonProcessingException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
