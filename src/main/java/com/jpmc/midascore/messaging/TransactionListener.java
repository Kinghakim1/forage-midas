package com.jpmc.midascore.messaging;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);

    private final TransactionService transactionService;

    // Spring automatically injects your TransactionService bean
    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void onMessage(String message) {
        log.info("Transaction received: {}", message);

        try {
            // Example message: "6, 7, 122.86"
            String[] parts = message.split(",");
            if (parts.length != 3) {
                log.warn("Invalid transaction format: {}", message);
                return;
            }

            long senderId = Long.parseLong(parts[0].trim());
            long recipientId = Long.parseLong(parts[1].trim());
            float amount = Float.parseFloat(parts[2].trim());

            // Create Transaction DTO from parsed values
            Transaction tx = new Transaction(senderId, recipientId, amount);

            // Process it through your service
            transactionService.process(tx);

        } catch (Exception e) {
            log.error("Failed to parse or process transaction: {}", message, e);
        }
    }
}