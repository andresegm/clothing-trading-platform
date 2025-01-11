package com.example.demo.initializer;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;
import java.util.Set;

@Component
public class AdminPasswordUpdater implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Ensure ADMIN role exists
        UserRole adminRole = userRoleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> userRoleRepository.save(new UserRole("ADMIN")));

        // Check if admin user exists
        Optional<User> adminOptional = userRepository.findByUsername("admin");
        if (adminOptional.isEmpty()) {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // Default password
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            System.out.println("Admin user created with default password: admin123");
        } else {
            User admin = adminOptional.get();
            // Update password if not already hashed
            if (!admin.getPassword().startsWith("$2a$")) {
                admin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(admin);
                System.out.println("Admin password updated to default password: admin123");
            }
        }
    }
}
