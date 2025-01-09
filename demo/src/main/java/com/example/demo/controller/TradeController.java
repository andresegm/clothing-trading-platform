package com.example.demo.controller;

import com.example.demo.dto.TradeDTO;
import com.example.demo.model.Trade;
import com.example.demo.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

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
    public ResponseEntity<TradeDTO> addTrade(@RequestBody Trade trade) {
        // Save trade and convert to DTO
        Trade savedTrade = tradeService.saveTrade(trade);
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
}
