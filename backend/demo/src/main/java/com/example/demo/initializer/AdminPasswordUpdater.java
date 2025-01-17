package com.example.demo.initializer;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminPasswordUpdater implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String adminUsername = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");

        if (adminUsername == null || adminPassword == null) {
            System.out.println("WARNING: Admin credentials not set. Skipping admin creation.");
            return;
        }

        // Ensure ADMIN role exists
        UserRole adminRole = userRoleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> userRoleRepository.save(new UserRole("ADMIN")));

        // Check if admin user exists
        Optional<User> adminOptional = userRepository.findByUsername(adminUsername);
        if (adminOptional.isEmpty()) {
            // Create admin user
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminUsername + "@example.com"); // Default email
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            System.out.println("Admin user created with username: " + adminUsername);
        } else {
            System.out.println("Admin user already exists. No changes made.");
        }
    }
}
