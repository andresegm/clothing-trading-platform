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
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/api/auth/register").permitAll() // Allow public registration (optional)

                        // Admin-Only Endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/user-roles/**").hasRole("ADMIN")

                        // User-Accessible Endpoints
                        .requestMatchers("/api/clothing-items/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/trades/**").hasAnyRole("USER", "ADMIN")

                        // All Other Endpoints
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {}); // Use HTTP Basic Authentication
        return http.build();
    }

}
