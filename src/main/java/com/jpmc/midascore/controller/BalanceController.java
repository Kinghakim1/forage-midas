package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class BalanceController {

    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            // Balance expects a float
            return new Balance(0f);
        }

        // Convert BigDecimal -> float
        float userBalance = user.getBalance().floatValue();

        return new Balance(userBalance);
    }
}