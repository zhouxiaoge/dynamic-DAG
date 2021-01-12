package com.zhouxiaoge.dag.kafka;

import cn.hutool.core.util.IdUtil;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author 周小哥
 * @date 2021年01月10日 19点11分
 */
public class KafkaUtils {

    // public static final String BOOTSTRAP_SERVERS = "192.168.124.13:9092,192.168.124.14:9092,192.168.124.15:9092";
    public static final String BOOTSTRAP_SERVERS = "192.168.245.149:9092,192.168.245.150:9092,192.168.245.151:9092";
    public static final String KAFKA_TOPIC = "zmy";

    public static Properties initKafkaProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        // props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

    public static Properties initKafkaConsumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, IdUtil.fastSimpleUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

    /**
     * 获取kafka的Topic列表
     *
     * @param properties kafka配置信息
     * @return topic集合
     */
    public static Set<String> getKafkaTopics(Properties properties) throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = KafkaAdminClient.create(properties).listTopics();
        KafkaFuture<Set<String>> names = listTopicsResult.names();
        return names.get();
    }


    public static List<PartitionInfo> getTopicPartitionInfoList(String topic) {
        Properties kafkaProperties = initKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        return consumer.partitionsFor(topic);
    }

    public static List<TopicPartition> getTopicPartitionList(String topic) {
        Properties kafkaProperties = initKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        return partitionInfos.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition())).collect(Collectors.toList());
    }
}
