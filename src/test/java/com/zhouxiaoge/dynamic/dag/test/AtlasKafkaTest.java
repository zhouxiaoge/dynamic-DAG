package com.zhouxiaoge.dynamic.dag.test;

import java.util.ArrayList;

import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dynamic.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dynamic.dag.tasks.impl.routers.KafkaConsumerRouter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.routers.KafkaProducerRouter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.storages.MemBasedTaskStorage;
import com.zhouxiaoge.dynamic.dag.jobs.PrintTaskJob;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.salesforce.kafka.test.junit4.SharedKafkaTestResource;

public class AtlasKafkaTest extends AtlasBaseTest {

    private static final int NUM_PARTITIONS = 1;

    private static final short REPLICATION_FACTOR = 1;

    @ClassRule
    public static final SharedKafkaTestResource sharedKafkaTestResource = //
            new SharedKafkaTestResource().withBrokers(1) //
                                         .withBrokerProperty("auto.create.topics.enable", "false");

    private void createTopic(String topicName) {
        var kafkaTestUtils = sharedKafkaTestResource.getKafkaTestUtils();
        kafkaTestUtils.createTopic(topicName, NUM_PARTITIONS, REPLICATION_FACTOR);
    }

    @Before
    public void init() throws PromiseException, InterruptedException {
        createTopic("atlas_router_test");
    }

    @Test
    public void testGraph() throws InterruptedException, PromiseException {
        var taskMapper = new DefaultTaskMapper().with("test-task", PrintTaskJob::new);
        var taskStorage = new MemBasedTaskStorage();
        var taskRouter = new KafkaProducerRouter(
                "kafka:atlas_router_test?mode=producer&brokers=" + sharedKafkaTestResource.getKafkaConnectString());
        var taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        var submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        submitter.start();

        var consumer = new KafkaConsumerRouter(
                "kafka:atlas_router_test?autoOffsetReset=earliest&groupId=test&mode=consumer&brokers="
                        + sharedKafkaTestResource.getKafkaConnectString(),
                taskRunner);
        consumer.start();

        var promises = new ArrayList<Promise<TaskResult, Throwable>>();
        for (var i = 0; i < 5; i++) {
            var batch = createBatch("test" + i);
            var promise = submitter.submitTasks(batch);
            promises.add(promise);
        }

        Promise.all(promises).get();

        consumer.stop();
        submitter.stop();
    }
}
