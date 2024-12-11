package ru.DTF98.TaskServiceWithAspect.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaTaskProducer {
    private final KafkaTemplate kafkaTemplate;

    public void sendTo(String topic, Object o) {
        try {
            kafkaTemplate.send(topic, o).get();
            kafkaTemplate.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
