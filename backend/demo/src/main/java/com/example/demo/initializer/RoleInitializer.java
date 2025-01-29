package com.example.demo.initializer;

import com.example.demo.repository.UserRoleRepository;
import com.example.demo.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleInitializer {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public void initializeRoles() {
        if (userRoleRepository.findByRoleName("ADMIN").isEmpty()) {
            UserRole adminRole = new UserRole();
            adminRole.setRoleName("ADMIN");
            userRoleRepository.save(adminRole);
        }

        if (userRoleRepository.findByRoleName("USER").isEmpty()) {
            UserRole userRole = new UserRole();
            userRole.setRoleName("USER");
            userRoleRepository.save(userRole);
        }
    }
}
