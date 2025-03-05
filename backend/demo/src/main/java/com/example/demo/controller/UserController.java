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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> getUserClothingItems(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        User user = userService.getUserEntityById(id);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("dateAdded").descending());
        Page<ClothingItem> clothingItems = clothingItemRepository.findAllByUserOrderByDateAddedDesc(user, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("items", clothingItems.getContent().stream()
                .map(item -> new ClothingItemDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getSize(),
                        item.getBrand(),
                        item.getCondition(),
                        item.getPrice(),
                        user.getId(),
                        item.isAvailable()
                ))
                .toList());
        response.put("totalItems", clothingItems.getTotalElements());
        response.put("totalPages", clothingItems.getTotalPages());
        response.put("currentPage", clothingItems.getNumber() + 1);

        return ResponseEntity.ok(response);
    }


    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}

