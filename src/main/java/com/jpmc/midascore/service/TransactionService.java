package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @Transactional
    public void process(Transaction t) {
        // Load users by numeric ids from the DTO
        User sender = userRepository.findById(t.getSenderId()).orElse(null);
        User recipient = userRepository.findById(t.getRecipientId()).orElse(null);
        if (sender == null || recipient == null) {
            return; // invalid sender/recipient -> discard
        }

        // Convert float -> BigDecimal safely
        BigDecimal amount = BigDecimal.valueOf(t.getAmount());

        // Validate amount and balance
        if (amount.signum() <= 0) {
            return; // non-positive amount -> discard
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            return; // insufficient funds -> discard
        }

        // Apply balance changes
        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        // Persist transaction record
        TransactionRecord rec = new TransactionRecord();
        rec.setSender(sender);
        rec.setRecipient(recipient);
        rec.setAmount(amount);

        transactionRecordRepository.save(rec);
        // Users will be flushed by JPA within the @Transactional boundary
    }
}