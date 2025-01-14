package com.example.demo.controller;

import com.example.demo.dto.TradeDTO;
import com.example.demo.model.Trade;
import com.example.demo.model.User;
import com.example.demo.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping
    public ResponseEntity<List<TradeDTO>> getTradesForUser(Principal principal) {
        // Fetch trades involving the logged-in user
        List<TradeDTO> trades = tradeService.getTradesForUser(principal.getName());
        return ResponseEntity.ok(trades);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TradeDTO> getTradeById(@PathVariable Long id) {
        TradeDTO trade = tradeService.getTradeById(id);
        return ResponseEntity.ok(trade);
    }

    @PostMapping
    public ResponseEntity<TradeDTO> addTrade(@RequestBody TradeDTO tradeDTO, Principal principal) {
        Trade newTrade = tradeService.createTrade(tradeDTO, principal.getName());
        return ResponseEntity.ok(tradeService.convertToDTO(newTrade));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TradeDTO> updateTradeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody,
            Principal principal
    ) {
        String action = requestBody.get("status"); // Extract status from the request body
        Trade updatedTrade = tradeService.updateTradeStatus(id, action, principal.getName());
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
            @RequestParam(required = false) LocalDateTime endDate
    )
    {
        List<TradeDTO> report = tradeService.generateTradeReport(status, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
