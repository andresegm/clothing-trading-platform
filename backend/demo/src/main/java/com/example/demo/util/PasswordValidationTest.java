package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordValidationTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String hashedPassword = "$2a$10$rKsxKLBPFq0iV3RhJz3I3.ILxhvynBIx6o.HZ4hu5zxF5PUCJkCJW"; // From DB

        System.out.println(encoder.matches(rawPassword, hashedPassword)); // Should print true
    }
}
