package com.example.demo.initializer;

import com.example.demo.initializer.RoleInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleInitializer roleInitializer;

    @Override
    public void run(String... args) throws Exception {
        roleInitializer.initializeRoles();
    }
}
