package com.example.demo.repository;

import com.example.demo.model.UserRole;
import java.util.Optional;

public interface UserRoleRepository extends BaseRepository<UserRole, Long> {
    Optional<UserRole> findByRoleName(String roleName);
}
