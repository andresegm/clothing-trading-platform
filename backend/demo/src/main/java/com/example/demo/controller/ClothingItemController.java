package com.example.demo.controller;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import com.example.demo.service.ClothingItemService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("/api/clothing-items")
public class ClothingItemController {

    @Autowired
    private ClothingItemService clothingItemService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ClothingItemDTO> addClothingItem(@Valid @RequestBody ClothingItemDTO clothingItemDTO, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ClothingItem clothingItem = new ClothingItem();
        clothingItem.setTitle(clothingItemDTO.getTitle());
        clothingItem.setDescription(clothingItemDTO.getDescription());
        clothingItem.setSize(clothingItemDTO.getSize());
        clothingItem.setBrand(clothingItemDTO.getBrand());
        clothingItem.setCondition(clothingItemDTO.getCondition());
        clothingItem.setPrice(clothingItemDTO.getPrice());
        clothingItem.setUser(user); // Assigns item to authenticated user

        ClothingItem savedItem = clothingItemService.saveClothingItem(clothingItem);
        return ResponseEntity.ok(clothingItemService.convertToDTO(savedItem));
    }


    @GetMapping
    public ResponseEntity<?> getAllClothingItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<ClothingItemDTO> itemsPage = clothingItemService.getAllClothingItems(page, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("items", itemsPage.getContent());
        response.put("totalItems", itemsPage.getTotalElements());
        response.put("totalPages", itemsPage.getTotalPages());
        response.put("currentPage", itemsPage.getNumber() + 1);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/filter")
    public ResponseEntity<?> filterClothingItems(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String size,  //
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int pageSize  //
    ) {
        Page<ClothingItemDTO> result = clothingItemService.filterClothingItems(
                title, brand, size, condition, minPrice, maxPrice, page, pageSize
        );

        Map<String, Object> response = new HashMap<>();
        response.put("items", result.getContent());
        response.put("totalItems", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        response.put("currentPage", result.getNumber() + 1);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClothingItemDTO> getClothingItemById(@PathVariable Long id) {
        ClothingItemDTO item = clothingItemService.getClothingItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClothingItemDTO> updateClothingItem(
            @PathVariable Long id, @Valid @RequestBody ClothingItemDTO clothingItemDTO) {
        ClothingItem updatedItem = clothingItemService.updateClothingItem(id, clothingItemDTO);
        return ResponseEntity.ok(clothingItemService.convertToDTO(updatedItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothingItem(@PathVariable Long id) {
        clothingItemService.deleteClothingItem(id);
        return ResponseEntity.noContent().build();
    }

    private ClothingItemDTO convertToDTO(ClothingItem item) {
        return new ClothingItemDTO(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getSize(),
                item.getBrand(),
                item.getCondition(),
                item.getPrice(),
                item.getUser().getId(),
                item.isAvailable()
        );
    }
}
