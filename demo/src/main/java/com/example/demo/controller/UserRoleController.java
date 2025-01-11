package com.example.demo.controller;

import com.example.demo.model.UserRole;
import com.example.demo.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<List<UserRole>> getAllRoles() {
        return ResponseEntity.ok(userRoleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRole> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(userRoleService.getRoleById(id));
    }

    @PostMapping
    public ResponseEntity<UserRole> addRole(@RequestBody UserRole role) {
        return ResponseEntity.ok(userRoleService.saveRole(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        userRoleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
