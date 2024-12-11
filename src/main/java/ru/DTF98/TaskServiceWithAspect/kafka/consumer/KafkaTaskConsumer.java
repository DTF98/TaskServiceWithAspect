package ru.DTF98.TaskServiceWithAspect.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.service.NotificationService;

import java.util.List;

import static ru.DTF98.TaskServiceWithAspect.kafka.constants.KafkaConfigConstants.DEFAULT_TOPIC;
import static ru.DTF98.TaskServiceWithAspect.kafka.constants.KafkaConfigConstants.GROUP_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTaskConsumer {
    private final NotificationService notificationService;

    @KafkaListener(id = GROUP_ID,
        topics = DEFAULT_TOPIC,
        containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<Task> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Task Consumer: обработка новых сообщений");
        log.info("Updated Task = {} from topic = {}", messageList, topic);
        try {
            notificationService.sendEmail(messageList.get(0));
        } finally {
            ack.acknowledge();
        }
    }
}
