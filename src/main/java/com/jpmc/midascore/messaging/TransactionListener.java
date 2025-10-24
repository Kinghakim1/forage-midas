package com.jpmc.midascore.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);

    @KafkaListener(
            topics = "${general.kafka-topic}"

    )
    public void onMessage(String message) {
        log.info("Transaction received: {}", message);
        // set your breakpoint on the line above
    }
}