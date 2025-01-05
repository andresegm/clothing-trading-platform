package com.example.demo.service;

import com.example.demo.model.ClothingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.ClothingItemRepository;

import java.util.List;

@Service
public class ClothingItemService {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    public ClothingItem saveClothingItem(ClothingItem clothingItem) {
        return clothingItemRepository.save(clothingItem);
    }

    public List<ClothingItem> getAllClothingItems() {
        return clothingItemRepository.findAll();
    }

    public ClothingItem getClothingItemById(Long id) {
        return clothingItemRepository.findById(id).orElseThrow(() -> new RuntimeException("ClothingItem not found with id: " + id));
    }

    public ClothingItem updateClothingItem(Long id, ClothingItem clothingItem) {
        ClothingItem existingItem = getClothingItemById(id);
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
}
