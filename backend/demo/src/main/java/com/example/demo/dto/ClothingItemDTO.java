package com.example.demo.dto;

public class ClothingItemDTO {

    private Long id;
    private String title;
    private String description;
    private String size;
    private String brand;
    private String condition;
    private Double price;
    private Long userId;

    // Constructor
    public ClothingItemDTO(Long id, String title, String description, String size, String brand,
                           String condition, Double price, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.size = size;
        this.brand = brand;
        this.condition = condition;
        this.price = price;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}