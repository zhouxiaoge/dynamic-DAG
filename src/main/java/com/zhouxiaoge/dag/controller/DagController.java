package com.zhouxiaoge.dag.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouxiaoge.dag.service.ExecService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.joo.promise4j.PromiseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@RestController
public class DagController {

    private final ExecService execService;

    public DagController(ExecService execService) {
        this.execService = execService;
    }

    @GetMapping("/syncDag/{variable}")
    public String syncDag(@PathVariable("variable") String variable, @RequestParam("map") Map<String, Object> map) throws PromiseException, InterruptedException {
        boolean b = execService.syncDagExecTask(variable, map);
        return String.valueOf(b);
    }

    @GetMapping("/asynDag/{variable}")
    public String asynDag(@PathVariable("variable") String variable, @RequestParam("map") Map<String, Object> map) throws PromiseException, InterruptedException {
        boolean b = execService.asynExecTask(variable, map);
        return String.valueOf(b);
    }

    @GetMapping("/kafka")
    public String syncDag() throws PromiseException, InterruptedException, JsonProcessingException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.124.13:9092");
        props.put("group.id", "GROUP_ID");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("zmy"));
        execService.generateTaskDependant();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("------------------------------Kafka启动拉取数据-" + finalI + "-----------------------------------");
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
                            boolean b = execService.asynExecTask("zhouxiaoge", map);
                            System.out.println(b);
                        } catch (JsonProcessingException | InterruptedException | PromiseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        return "DAG 启动成功";
    }
}
