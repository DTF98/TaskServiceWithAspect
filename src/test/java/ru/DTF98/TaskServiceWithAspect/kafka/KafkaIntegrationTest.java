package ru.DTF98.TaskServiceWithAspect.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.testContainer.FullIntegrationPropertyTestContainer;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaIntegrationTest extends FullIntegrationPropertyTestContainer {
    private static final String TEST_TOPIC = "newTaskTopic";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Тест отправки и получения сообщения с объектом Task через Kafka")
    public void testKafkaProducerAndConsumer() throws ExecutionException, InterruptedException {
        Task task = new Task(1L, "Test Task", "This is a test task description", 100L);
        String taskJson = null;
        try {
            taskJson = objectMapper.writeValueAsString(task);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties())) {
            String key = "testKey";
            ProducerRecord<String, String> record = new ProducerRecord<>(TEST_TOPIC, key, taskJson);
            RecordMetadata metadata = producer.send(record).get();

            assertThat(metadata.topic()).isEqualTo(TEST_TOPIC);
        }

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties())) {
            consumer.subscribe(Collections.singletonList(TEST_TOPIC));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            assertThat(records.count()).isGreaterThan(0);

            for (ConsumerRecord<String, String> record : records) {
                Task receivedTask = null;
                try {
                    receivedTask = objectMapper.readValue(record.value(), Task.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                assertThat(record.key()).isEqualTo("testKey");
                assertThat(receivedTask).isNotNull();
                assertThat(receivedTask.getId()).isEqualTo(task.getId());
                assertThat(receivedTask.getTitle()).isEqualTo(task.getTitle());
                assertThat(receivedTask.getDescription()).isEqualTo(task.getDescription());
                assertThat(receivedTask.getUserId()).isEqualTo(task.getUserId());
            }
        }
    }
}
