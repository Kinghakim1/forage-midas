package com.jpmc.midascore.bootstrap;


import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile({"default", "test"}) // ensures this runs in dev/test environments
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        seedUser("waldorf", new BigDecimal("1000"));
        seedUser("statler", new BigDecimal("1000"));
    }

    private void seedUser(String username, BigDecimal balance) {
        userRepository.findByUsername(username).orElseGet(() -> {
            User u = new User();
            u.setUsername(username);
            u.setBalance(balance);
            return userRepository.save(u);
        });
    }
}
