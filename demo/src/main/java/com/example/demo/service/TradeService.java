package com.example.demo.service;

import com.example.demo.dto.TradeDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.Trade;
import com.example.demo.model.User;
import com.example.demo.repository.ClothingItemRepository;
import com.example.demo.repository.TradeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private ClothingItemRepository clothingItemRepository; // Inject ClothingItemRepository

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    public Trade saveTrade(Trade trade) {
        // Fetch related entities to ensure they are properly linked
        trade.setItem(fetchClothingItem(trade.getItem().getId()));
        trade.setInitiator(fetchUser(trade.getInitiator().getId()));
        trade.setReceiver(fetchUser(trade.getReceiver().getId()));

        return tradeRepository.save(trade);
    }

    private ClothingItem fetchClothingItem(Long itemId) {
        // Logic to fetch ClothingItem by ID
        return clothingItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("ClothingItem not found with id: " + itemId));
    }

    private User fetchUser(Long userId) {
        // Logic to fetch User by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public List<TradeDTO> getAllTrades() {
        return tradeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TradeDTO getTradeById(Long id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found with id: " + id));
        return convertToDTO(trade);
    }

    public Trade updateTrade(Long id, Trade trade) {
        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found with id: " + id));

        existingTrade.setStatus(trade.getStatus());
        existingTrade.setItem(fetchClothingItem(trade.getItem().getId())); // Fetch item
        existingTrade.setInitiator(fetchUser(trade.getInitiator().getId())); // Fetch initiator
        existingTrade.setReceiver(fetchUser(trade.getReceiver().getId())); // Fetch receiver

        return tradeRepository.save(existingTrade);
    }


    public void deleteTrade(Long id) {
        tradeRepository.deleteById(id);
    }

    public TradeDTO convertToDTO(Trade trade) {
        return new TradeDTO(
                trade.getId(),
                trade.getStatus(),
                trade.getItem().getId(),
                trade.getItem().getTitle(),
                trade.getInitiator().getId(),
                trade.getInitiator().getUsername(),
                trade.getReceiver().getId(),
                trade.getReceiver().getUsername()
        );
    }
}

