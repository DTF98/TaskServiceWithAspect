package ru.DTF98.TaskServiceWithAspect.testContainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;

@Testcontainers
public class FullIntegrationPropertyTestContainer {
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:5.5.0");

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Container
    protected static final KafkaContainer KAFKA_CONTAINER
            = new KafkaContainer(KAFKA_IMAGE);

    @Container
    public static GenericContainer<?> mailHogContainer = new GenericContainer<>("mailhog/mailhog:v1.0.1")
            .withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // Настройка базы данных PostgreSQL
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        //Настройка Kafka
        registry.add("kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("kafka.group-id", () -> "test_group");
        registry.add("kafka.topic", () -> "newTaskTopic");

        // Настройка JavaMailSender для MailHog
        registry.add("spring.mail.host", mailHogContainer::getHost);
        registry.add("spring.mail.port", () -> mailHogContainer.getMappedPort(1025));
    }

    public static Properties producerProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_CONTAINER.getBootstrapServers());
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public static Properties consumerProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_CONTAINER.getBootstrapServers());
        properties.put("group.id", "test_group");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("auto.offset.reset", "earliest");
        return properties;
    }
}
