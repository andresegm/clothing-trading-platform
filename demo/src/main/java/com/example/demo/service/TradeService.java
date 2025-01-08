package com.example.demo.service;

import com.example.demo.dto.TradeDTO;
import com.example.demo.model.Trade;
import com.example.demo.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public Trade saveTrade(Trade trade) {
        return tradeRepository.save(trade);
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
        existingTrade.setItem(trade.getItem());
        existingTrade.setInitiator(trade.getInitiator());
        existingTrade.setReceiver(trade.getReceiver());
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
