package com.example.demo.controller;

import com.example.demo.dto.TradeDTO;
import com.example.demo.model.ClothingItem;
import com.example.demo.model.Trade;
import com.example.demo.model.User;
import com.example.demo.repository.BaseRepository;
import com.example.demo.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private BaseRepository<ClothingItem, Long> clothingItemRepository;

    @Autowired
    private BaseRepository<User, Long> userRepository;

    @GetMapping
    public ResponseEntity<List<TradeDTO>> getAllTrades() {
        List<TradeDTO> trades = tradeService.getAllTrades();
        return ResponseEntity.ok(trades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeDTO> getTradeById(@PathVariable Long id) {
        TradeDTO trade = tradeService.getTradeById(id);
        return ResponseEntity.ok(trade);
    }

    @PostMapping
    public ResponseEntity<TradeDTO> addTrade(@RequestBody TradeDTO tradeDTO) {
        // Create a new Trade object
        Trade trade = new Trade();
        trade.setStatus(tradeDTO.getStatus());

        // Fetch and set the ClothingItem
        ClothingItem item = clothingItemRepository.findById(tradeDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Clothing item not found with id: " + tradeDTO.getItemId()));
        trade.setItem(item);

        // Fetch and set the Initiator
        User initiator = userRepository.findById(tradeDTO.getInitiatorId())
                .orElseThrow(() -> new RuntimeException("Initiator user not found with id: " + tradeDTO.getInitiatorId()));
        trade.setInitiator(initiator);

        // Fetch and set the Receiver
        User receiver = userRepository.findById(tradeDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver user not found with id: " + tradeDTO.getReceiverId()));
        trade.setReceiver(receiver);

        // Save the trade
        Trade savedTrade = tradeService.saveTrade(trade);

        // Convert to DTO and return response
        return ResponseEntity.ok(tradeService.convertToDTO(savedTrade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeDTO> updateTrade(@PathVariable Long id, @RequestBody Trade trade) {
        Trade updatedTrade = tradeService.updateTrade(id, trade);
        return ResponseEntity.ok(tradeService.convertToDTO(updatedTrade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long id) {
        tradeService.deleteTrade(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report")
    public ResponseEntity<List<TradeDTO>> generateTradeReport(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        List<TradeDTO> report = tradeService.generateTradeReport(status, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
