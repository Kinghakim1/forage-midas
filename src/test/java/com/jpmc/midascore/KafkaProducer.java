package com.jpmc.midascore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                         @Value("${general.kafka-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(String message) {
        kafkaTemplate.send(topic, message);
    }
}