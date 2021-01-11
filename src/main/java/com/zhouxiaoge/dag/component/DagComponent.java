package com.zhouxiaoge.dag.component;

import com.zhouxiaoge.dag.kafka.DagKafkaConsumer;
import com.zhouxiaoge.dag.service.ExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 周小哥
 * @date 2021年01月11日 21点36分
 */
@Component
public class DagComponent {

    private final ExecService execService;

    @Autowired
    private DagKafkaConsumer dagKafkaConsumer;

    public DagComponent(ExecService execService) {
        this.execService = execService;
    }

    public void startDag(String dagKey) {
        execService.generateTaskDependant(dagKey);
        dagKafkaConsumer.consumer(dagKey);
    }
}
