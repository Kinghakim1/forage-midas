package com.jpmc.midascore;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserPopulator {

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    public void populate() {
        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");
        for (String userLine : userLines) {
            try {
                // Each line looks like: "username, 1000.0"
                String[] userData = userLine.split(",\\s*");
                if (userData.length != 2) {
                    continue; // skip malformed lines
                }

                String username = userData[0].trim();
                BigDecimal balance = new BigDecimal(userData[1].trim());

                // Avoid duplicates if already exists
                userRepository.findByUsername(username).orElseGet(() -> {
                    User user = new User();
                    user.setUsername(username);
                    user.setBalance(balance);
                    return userRepository.save(user);
                });
            } catch (Exception e) {
                System.err.println("Error parsing user line: " + userLine + " -> " + e.getMessage());
            }
        }
    }
}