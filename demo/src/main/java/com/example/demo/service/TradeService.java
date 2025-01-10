package com.example.demo.service;

import com.example.demo.dto.TradeDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.Trade;
import com.example.demo.model.User;
import com.example.demo.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private BaseRepository<Trade, Long> tradeRepository;

    @Autowired
    private BaseRepository<ClothingItem, Long> clothingItemRepository;

    @Autowired
    private BaseRepository<User, Long> userRepository;

    public Trade saveTrade(Trade trade) {
        // Fetch related entities to ensure they are properly linked
        trade.setItem(fetchEntity(clothingItemRepository, trade.getItem().getId(), "ClothingItem"));
        trade.setInitiator(fetchEntity(userRepository, trade.getInitiator().getId(), "User"));
        trade.setReceiver(fetchEntity(userRepository, trade.getReceiver().getId(), "User"));

        // Set the tradeDate to the current time
        trade.setTradeDate(LocalDateTime.now());

        return tradeRepository.save(trade);
    }

    public Trade updateTrade(Long id, Trade trade) {
        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found with id: " + id));

        // Update fields
        existingTrade.setStatus(trade.getStatus());
        existingTrade.setItem(fetchEntity(clothingItemRepository, trade.getItem().getId(), "ClothingItem"));
        existingTrade.setInitiator(fetchEntity(userRepository, trade.getInitiator().getId(), "User"));
        existingTrade.setReceiver(fetchEntity(userRepository, trade.getReceiver().getId(), "User"));

        // Update the tradeDate to the current time
        existingTrade.setTradeDate(LocalDateTime.now());

        return tradeRepository.save(existingTrade);
    }

    private <T> T fetchEntity(BaseRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " not found with id: " + id));
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

    public void deleteTrade(Long id) {
        tradeRepository.deleteById(id);
    }

    public List<TradeDTO> generateTradeReport(String status, LocalDateTime startDate, LocalDateTime endDate) {
        // Filter trades based on the provided criteria
        return tradeRepository.findAll().stream()
                .filter(trade -> (status == null || trade.getStatus().equalsIgnoreCase(status)) &&
                        (startDate == null || !trade.getTradeDate().isBefore(startDate)) &&
                        (endDate == null || !trade.getTradeDate().isAfter(endDate)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
