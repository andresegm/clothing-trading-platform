package com.example.demo.repository;

import com.example.demo.model.ClothingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {
}
