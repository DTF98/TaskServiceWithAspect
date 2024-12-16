package ru.DTF98.TaskServiceWithAspect.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaTaskProducer {
    private final KafkaTemplate kafkaTemplate;

    public void sendTo(String topic, Object o) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, o);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + o +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        o + "] due to : " + ex.getMessage());
            }
        });
    }
}
