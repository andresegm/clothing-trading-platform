/*
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all paths
                        .allowedOrigins("http://localhost:4200") // Frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods
                        .allowedHeaders("Authorization", "Content-Type", "Accept", "X-Requested-With", "Origin") // Allow all headers
                        .allowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)
            }
        };
    }
}
*/