package com.example.demo.service;

import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    public UserRole getRoleById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    public UserRole saveRole(UserRole role) {
        return userRoleRepository.save(role);
    }

    public void deleteRole(Long id) {
        userRoleRepository.deleteById(id);
    }
}
