package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.component.KafkaComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 周小哥
 * @date 2021年01月25日 21点59分
 */

@RequestMapping("/kafka")
@RestController
public class KafkaController {

    private final KafkaComponent kafkaComponent;

    public KafkaController(KafkaComponent kafkaComponent) {
        this.kafkaComponent = kafkaComponent;
    }

    @GetMapping("/start/{dagKey}")
    public void startKafka(@PathVariable("dagKey") String dagKey) {
        kafkaComponent.startKafka(dagKey);
    }

    @GetMapping("/stop/{dagKey}")
    public void stopKafka(@PathVariable("dagKey") String dagKey) {
        kafkaComponent.stopKafka(dagKey);
    }
}
