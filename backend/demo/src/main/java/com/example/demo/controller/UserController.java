package com.example.demo.controller;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import com.example.demo.repository.ClothingItemRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(convertToDTO(savedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/clothing-items")
    public ResponseEntity<List<ClothingItemDTO>> getUserClothingItems(@PathVariable Long id) {
        // Fetch the User entity by ID
        User user = userService.getUserEntityById(id);

        // Fetch the clothing items for this user
        List<ClothingItem> clothingItems = clothingItemRepository.findTop10ByUserOrderByDateAddedDesc(user);

        // Convert entities to DTOs
        List<ClothingItemDTO> clothingItemDTOs = clothingItems.stream()
                .map(item -> new ClothingItemDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getSize(),
                        item.getBrand(),
                        item.getCondition(),
                        item.getPrice(),
                        user.getId(),
                        item.isAvailable()// Include user ID in DTO
                ))
                .toList();

        // Return the response
        return ResponseEntity.ok(clothingItemDTOs);
    }



    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}

