package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.BaseRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private BaseRepository<User, Long> userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public User saveUser(User user) {
        List<UserRole> userRoles = user.getRoles().stream()
                .map(role -> {
                    // Check if the role already exists
                    return userRoleRepository.findByRoleName(role.getRoleName())
                            .orElseGet(() -> {
                                // If it doesn't exist, save it
                                UserRole newRole = new UserRole();
                                newRole.setRoleName(role.getRoleName());
                                return userRoleRepository.save(newRole);
                            });
                })
                .collect(Collectors.toList());

        user.setRoles(userRoles);
        return userRepository.save(user);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public User updateUser(Long id, User user) {
        User existingUser = fetchEntity(userRepository, id, "User");
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserEntityById(Long id) {
        return fetchEntity(userRepository, id, "User");
    }

    public UserDTO getUserById(Long id) {
        return convertToDTO(fetchEntity(userRepository, id, "User"));
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    private <T> T fetchEntity(BaseRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " not found with id: " + id));
    }
}
