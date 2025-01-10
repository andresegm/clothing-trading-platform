package com.example.demo.controller;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import com.example.demo.service.ClothingItemService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clothing-items")
public class ClothingItemController {

    @Autowired
    private ClothingItemService clothingItemService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ClothingItemDTO> addClothingItem(@Valid @RequestBody ClothingItemDTO clothingItemDTO) {
        User user = userService.getUserEntityById(clothingItemDTO.getUserId());
        ClothingItem clothingItem = new ClothingItem();
        clothingItem.setTitle(clothingItemDTO.getTitle());
        clothingItem.setDescription(clothingItemDTO.getDescription());
        clothingItem.setSize(clothingItemDTO.getSize());
        clothingItem.setBrand(clothingItemDTO.getBrand());
        clothingItem.setCondition(clothingItemDTO.getCondition());
        clothingItem.setPrice(clothingItemDTO.getPrice());
        clothingItem.setUser(user);
        ClothingItem savedItem = clothingItemService.saveClothingItem(clothingItem);
        return ResponseEntity.ok(clothingItemService.convertToDTO(savedItem));
    }

    @GetMapping
    public ResponseEntity<List<ClothingItemDTO>> getAllClothingItems() {
        List<ClothingItemDTO> items = clothingItemService.getAllClothingItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ClothingItemDTO>> filterClothingItems(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ClothingItemDTO> items = clothingItemService.filterClothingItems(title, brand, size, condition, minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothingItemDTO> getClothingItemById(@PathVariable Long id) {
        ClothingItemDTO item = clothingItemService.getClothingItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClothingItemDTO> updateClothingItem(@PathVariable Long id, @RequestBody ClothingItem clothingItem) {
        ClothingItem updatedItem = clothingItemService.updateClothingItem(id, clothingItem);
        return ResponseEntity.ok(convertToDTO(updatedItem));
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
                item.getUser().getId()
        );
    }
}
