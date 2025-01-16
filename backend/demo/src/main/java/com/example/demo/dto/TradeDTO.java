package com.example.demo.dto;

import java.time.LocalDateTime;

public class TradeDTO {

    private Long id;
    private String status;
    private Long itemId;
    private String itemTitle;
    private Long initiatorId;
    private String initiatorUsername;
    private String initiatorEmail;
    private Long receiverId;
    private String receiverUsername;
    private LocalDateTime tradeDate;

    // Updated Constructor
    public TradeDTO(Long id, String status, Long itemId, String itemTitle,
                    Long initiatorId, String initiatorUsername,
                    String initiatorEmail, Long receiverId,
                    String receiverUsername, LocalDateTime tradeDate) {
        this.id = id;
        this.status = status;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.initiatorId = initiatorId;
        this.initiatorUsername = initiatorUsername;
        this.initiatorEmail = initiatorEmail;
        this.receiverId = receiverId;
        this.receiverUsername = receiverUsername;
        this.tradeDate = tradeDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getInitiatorUsername() {
        return initiatorUsername;
    }

    public void setInitiatorUsername(String initiatorUsername) {
        this.initiatorUsername = initiatorUsername;
    }

    public String getInitiatorEmail() {
        return initiatorEmail;
    }

    public void setInitiatorEmail(String initiatorEmail) {
        this.initiatorEmail = initiatorEmail;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public LocalDateTime getTradeDate() {
        return tradeDate; // Getter for tradeDate
    }

    public void setTradeDate(LocalDateTime tradeDate) {
        this.tradeDate = tradeDate; // Setter for tradeDate
    }
}
