package com.example.demo.service;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import com.example.demo.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClothingItemService {

    @Autowired
    private BaseRepository<ClothingItem, Long> clothingItemRepository;

    @Autowired
    private BaseRepository<User, Long> userRepository;

    public ClothingItem saveClothingItem(ClothingItem clothingItem) {
        // Validate and fetch associated User
        clothingItem.setUser(fetchEntity(userRepository, clothingItem.getUser().getId(), "User"));
        return clothingItemRepository.save(clothingItem);
    }

    public List<ClothingItemDTO> getAllClothingItems() {
        return clothingItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClothingItemDTO getClothingItemById(Long id) {
        return convertToDTO(fetchEntity(clothingItemRepository, id, "ClothingItem"));
    }

    public ClothingItem updateClothingItem(Long id, ClothingItem clothingItem) {
        ClothingItem existingItem = fetchEntity(clothingItemRepository, id, "ClothingItem");
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

    private <T> T fetchEntity(BaseRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " not found with id: " + id));
    }
}
