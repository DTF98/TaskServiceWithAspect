package ru.DTF98.TaskServiceWithAspect.kafka.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try {
            return super.deserialize(topic, headers, data);
        } catch (Exception ex) {
            log.warn("Произошла ошибка о время десериализации сообщения {}", new String(data, StandardCharsets.UTF_8));
            return null;
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception ex) {
            log.warn("Произошла ошибка о время десериализации сообщения {}", new String(data, StandardCharsets.UTF_8));
            return null;
        }
    }
}
