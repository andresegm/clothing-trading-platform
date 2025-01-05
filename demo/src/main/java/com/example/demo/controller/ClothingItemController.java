package com.example.demo.controller;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.service.ClothingItemService;
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

    @PostMapping
    public ResponseEntity<ClothingItemDTO> addClothingItem(@Valid @RequestBody ClothingItem clothingItem) {
        ClothingItem savedItem = clothingItemService.saveClothingItem(clothingItem);
        ClothingItemDTO dto = new ClothingItemDTO(
                savedItem.getId(),
                savedItem.getTitle(),
                savedItem.getDescription(),
                savedItem.getSize(),
                savedItem.getBrand(),
                savedItem.getCondition(),
                savedItem.getPrice(),
                savedItem.getUser().getId() // Include userId in the DTO
        );
        return ResponseEntity.ok(dto);
    }


    @GetMapping
    public ResponseEntity<List<ClothingItemDTO>> getAllClothingItems() {
        List<ClothingItem> items = clothingItemService.getAllClothingItems();
        List<ClothingItemDTO> dtos = items.stream()
                .map(item -> new ClothingItemDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getSize(),
                        item.getBrand(),
                        item.getCondition(),
                        item.getPrice(),
                        item.getUser().getId()
                ))
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClothingItem> getClothingItemById(@PathVariable Long id) {
        return ResponseEntity.ok(clothingItemService.getClothingItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClothingItem> updateClothingItem(@PathVariable Long id, @RequestBody ClothingItem clothingItem) {
        return ResponseEntity.ok(clothingItemService.updateClothingItem(id, clothingItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothingItem(@PathVariable Long id) {
        clothingItemService.deleteClothingItem(id);
        return ResponseEntity.noContent().build();
    }
}
