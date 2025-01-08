package com.example.demo.service;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.ClothingItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClothingItemService {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private UserService userService;

    public ClothingItem saveClothingItem(ClothingItem clothingItem) {
        // Validate that the User exists
        if (clothingItem.getUser() == null || clothingItem.getUser().getId() == null) {
            throw new IllegalArgumentException("User must be associated with the clothing item.");
        }

        // Fetch the User entity
        User user = userService.getUserEntityById(clothingItem.getUser().getId());
        clothingItem.setUser(user); // Reassociate the validated User

        // Save the clothing item
        return clothingItemRepository.save(clothingItem);
    }

    public List<ClothingItemDTO> getAllClothingItems() {
        return clothingItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClothingItemDTO getClothingItemById(Long id) {
        ClothingItem item = clothingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClothingItem not found with id: " + id));
        return convertToDTO(item);
    }

    public ClothingItem updateClothingItem(Long id, ClothingItem clothingItem) {
        ClothingItem existingItem = clothingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClothingItem not found with id: " + id));
        existingItem.setTitle(clothingItem.getTitle());
        existingItem.setDescription(clothingItem.getDescription());
        existingItem.setSize(clothingItem.getSize());
        existingItem.setBrand(clothingItem.getBrand());
        existingItem.setCondition(clothingItem.getCondition());
        existingItem.setPrice(clothingItem.getPrice());
        return clothingItemRepository.save(existingItem);
    }

    public void deleteClothingItem(Long id) {
        clothingItemRepository.deleteById(id);
    }

    public ClothingItemDTO convertToDTO(ClothingItem item) {
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
