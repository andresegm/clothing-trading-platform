package com.example.demo.service;

import com.example.demo.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.TradeRepository;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public Trade saveTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    public Trade getTradeById(Long id) {
        return tradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Trade not found with id: " + id));
    }

    public Trade updateTrade(Long id, Trade trade) {
        Trade existingTrade = getTradeById(id);
        existingTrade.setStatus(trade.getStatus());
        existingTrade.setItem(trade.getItem());
        existingTrade.setInitiator(trade.getInitiator());
        existingTrade.setReceiver(trade.getReceiver());
        return tradeRepository.save(existingTrade);
    }

    public void deleteTrade(Long id) {
        tradeRepository.deleteById(id);
    }
}
