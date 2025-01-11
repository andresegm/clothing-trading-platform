package com.example.demo.dto;

public class TradeDTO {

    private Long id;
    private String status;
    private Long itemId;
    private String itemTitle;
    private Long initiatorId;
    private String initiatorUsername;
    private Long receiverId;
    private String receiverUsername;

    // Constructor
    public TradeDTO(Long id, String status, Long itemId, String itemTitle,
                    Long initiatorId, String initiatorUsername,
                    Long receiverId, String receiverUsername) {
        this.id = id;
        this.status = status;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.initiatorId = initiatorId;
        this.initiatorUsername = initiatorUsername;
        this.receiverId = receiverId;
        this.receiverUsername = receiverUsername;
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
}
