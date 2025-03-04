package com.example.demo.service;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.User;
import com.example.demo.repository.BaseRepository;
import com.example.demo.repository.ClothingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;


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

    public Page<ClothingItemDTO> getAllClothingItems(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("dateAdded").descending());
        Page<ClothingItem> itemsPage = clothingItemRepository.findAll(pageable);
        return itemsPage.map(this::convertToDTO);
    }



    public ClothingItemDTO getClothingItemById(Long id) {
        return convertToDTO(fetchEntity(clothingItemRepository, id, "ClothingItem"));
    }

    public Page<ClothingItemDTO> filterClothingItems(
            String title, String brand, String size, String condition,
            Double minPrice, Double maxPrice, int page, int pageSize) {

        logger.debug("Filtering clothing items with pagination - title: {}, brand: {}, size: {}, condition: {}, minPrice: {}, maxPrice: {}, page: {}, pageSize: {}",
                title, brand, size, condition, minPrice, maxPrice, page, pageSize);

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("dateAdded").descending());
        Page<ClothingItem> items = clothingItemRepository.filterClothingItems(title, brand, size, condition, minPrice, maxPrice, pageable);

        return items.map(this::convertToDTO);
    }



    public ClothingItem updateClothingItem(Long id, ClothingItemDTO clothingItemDTO) {
        ClothingItem item = clothingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clothing item not found with id: " + id));
        item.setTitle(clothingItemDTO.getTitle());
        item.setDescription(clothingItemDTO.getDescription());
        item.setSize(clothingItemDTO.getSize());
        item.setBrand(clothingItemDTO.getBrand());
        item.setCondition(clothingItemDTO.getCondition());
        item.setPrice(clothingItemDTO.getPrice());
        item.setAvailable(clothingItemDTO.isAvailable());

        return clothingItemRepository.save(item);
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

    public List<ClothingItemDTO> findRecentByUser(User user, Pageable pageable) {
        return clothingItemRepository.findTop6ByUserOrderByDateAddedDesc(user, pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClothingItemDTO> findAllByUser(User user, Pageable pageable) {
        return clothingItemRepository.findAllByUserOrderByDateAddedDesc(user, pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
