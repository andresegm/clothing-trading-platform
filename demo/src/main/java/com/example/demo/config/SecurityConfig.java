package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Updated CSRF configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Public endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN") // Restrict to ADMIN
                        .requestMatchers("/api/user-roles/**").hasRole("ADMIN") // Restrict to ADMIN
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .httpBasic(httpBasic -> {});
        return http.build();
    }
}
