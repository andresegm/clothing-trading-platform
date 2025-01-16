package com.example.demo.service;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import com.example.demo.repository.BaseRepository;
import com.example.demo.repository.ClothingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClothingItemService {
    private static final Logger logger = LoggerFactory.getLogger(ClothingItemService.class);

    @Autowired
    private ClothingItemRepository clothingItemRepository;

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

    public List<ClothingItemDTO> filterClothingItems(String title, String brand, String size, String condition, Double minPrice, Double maxPrice) {
        logger.debug("Filtering clothing items with params - title: {}, brand: {}, size: {}, condition: {}, minPrice: {}, maxPrice: {}",
                title, brand, size, condition, minPrice, maxPrice);

        List<ClothingItem> items = clothingItemRepository.filterClothingItems(title, brand, size, condition, minPrice, maxPrice);

        if (items.isEmpty()) {
            logger.warn("No clothing items found matching the given criteria.");
        } else {
            logger.debug("Found {} clothing items matching the criteria.", items.size());
        }

        return items.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
                item.getUser().getId(),
                item.isAvailable()
        );
    }

    private <T> T fetchEntity(BaseRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " not found with id: " + id));
    }

    public List<ClothingItem> findRecentByUser(User user) {
        return clothingItemRepository.findTop10ByUserOrderByDateAddedDesc(user);
    }
}
