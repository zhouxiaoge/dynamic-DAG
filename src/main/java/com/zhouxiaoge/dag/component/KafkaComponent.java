package com.zhouxiaoge.dag.component;

import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.kafka.MyKafkaConsumer;
import com.zhouxiaoge.dag.kafka.KafkaConsumerThread;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月25日 21点52分
 */
@Component
public class KafkaComponent {

    private final MyKafkaConsumer kafkaConsumer;

    public KafkaComponent(MyKafkaConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    public void startKafka(String dagKey) {
        kafkaConsumer.consumer(dagKey);
    }

    public void stopKafka(String dagKey) {
        List<KafkaConsumerThread> kafkaConsumerThreadList = DagCacheUtils.getKafkaConsumerThreadList(dagKey);
        for (KafkaConsumerThread kafkaConsumerThread : kafkaConsumerThreadList) {
            kafkaConsumerThread.shutdown();
        }
    }
}
