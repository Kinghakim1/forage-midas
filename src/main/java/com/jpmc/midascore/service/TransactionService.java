package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.model.Incentive;    // <-- add this import
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository,
                              RestTemplate restTemplate) {

        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }


    // STEP 3: Incentive API call

    private Incentive fetchIncentive(Transaction t) {
        String url = "http://localhost:8080/incentive";
        return restTemplate.postForObject(url, t, Incentive.class);
    }

    @Transactional
    public void process(Transaction t) {

        User sender = userRepository.findById(t.getSenderId()).orElse(null);
        User recipient = userRepository.findById(t.getRecipientId()).orElse(null);
        if (sender == null || recipient == null) {
            return;
        }

        BigDecimal amount = BigDecimal.valueOf(t.getAmount());

        if (amount.signum() <= 0) {
            return;
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            return;
        }


        // STEP 3: Fetch incentive

        Incentive incentive = fetchIncentive(t);
        double incentiveAmount = incentive != null ? incentive.getAmount() : 0.0;
        BigDecimal incentiveBD = BigDecimal.valueOf(incentiveAmount);


        // Balance updates

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount).add(incentiveBD));


        //  Persist transaction record

        TransactionRecord rec = new TransactionRecord();
        rec.setSender(sender);
        rec.setRecipient(recipient);
        rec.setAmount(amount);


        transactionRecordRepository.save(rec);
    }
}