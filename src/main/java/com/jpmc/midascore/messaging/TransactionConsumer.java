package com.jpmc.midascore.messaging;

import com.jpmc.midascore.service.TransactionService;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//@Component
/* public class TransactionConsumer {

    private final TransactionService service;

    public TransactionConsumer(TransactionService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${midas.kafka.transactions-topic:transactions}", groupId = "midas-core")
    public void onMessage(Transaction t) {
        service.process(t);
    }
}
 */