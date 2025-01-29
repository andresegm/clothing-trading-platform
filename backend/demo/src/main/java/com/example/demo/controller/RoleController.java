package com.example.demo.controller;

import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public ResponseEntity<List<UserRole>> getAllRoles() {
        return ResponseEntity.ok(userRoleRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<UserRole> addRole(@RequestBody UserRole role) {
        if (userRoleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userRoleRepository.save(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        userRoleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
