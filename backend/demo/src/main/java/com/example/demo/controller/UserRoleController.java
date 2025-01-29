package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserRoleMappingDTO>> getAllUserRoles() {
        List<User> users = userRepository.findAll();

        // Map users and their roles to a DTO
        List<UserRoleMappingDTO> userRoles = users.stream()
                .flatMap(user -> user.getRoles().stream()
                        .map(role -> new UserRoleMappingDTO(user.getId(), user.getUsername(), role.getId(), role.getRoleName())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userRoles);
    }

    // DTO to represent the user-role mapping
    public static class UserRoleMappingDTO {
        private Long userId;
        private String username;
        private Long roleId;
        private String roleName;

        public UserRoleMappingDTO(Long userId, String username, Long roleId, String roleName) {
            this.userId = userId;
            this.username = username;
            this.roleId = roleId;
            this.roleName = roleName;
        }

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }
}
