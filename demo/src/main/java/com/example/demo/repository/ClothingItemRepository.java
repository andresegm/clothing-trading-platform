package com.example.demo.repository;

import com.example.demo.model.ClothingItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClothingItemRepository extends BaseRepository<ClothingItem, Long> {

    @Query("SELECT c FROM ClothingItem c WHERE " +
            "(LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL) AND " +
            "(LOWER(c.brand) LIKE LOWER(CONCAT('%', :brand, '%')) OR :brand IS NULL) AND " +
            "(LOWER(c.size) LIKE LOWER(CONCAT('%', :size, '%')) OR :size IS NULL) AND " +
            "(LOWER(c.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR :condition IS NULL) AND " +
            "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR c.price <= :maxPrice)")
    List<ClothingItem> filterClothingItems(
            @Param("title") String title,
            @Param("brand") String brand,
            @Param("size") String size,
            @Param("condition") String condition,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

}
