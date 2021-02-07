package com.zhouxiaoge.dag.kaka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouxiaoge.dag.kafka.KafkaUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class KafkaProducerTest {


    public static void main(String[] args) throws JsonProcessingException {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaUtils.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.initTransactions();
        try {
            producer.beginTransaction();
            for (int i = 1; i <= 500000; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("ID", i);
                map.put("NAME", "zhouxiaoge-" + i);
                map.put("AGE", (int) (Math.random() * 100));
                map.put("SEX", i % 2);
                ObjectMapper objectMapper = new ObjectMapper();
                String s = objectMapper.writeValueAsString(map);
                System.out.println("-------------------------------------------------------");
                System.out.println(s);
                producer.send(new ProducerRecord<>(KafkaUtils.KAFKA_TOPIC, Integer.toString(i), s));
            }
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            producer.close();
        } catch (KafkaException e) {
            producer.abortTransaction();
        }
    }
}