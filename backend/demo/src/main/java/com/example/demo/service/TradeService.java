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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public Trade saveTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    public Trade updateTrade(Long id, Trade trade) {
        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found with id: " + id));
        existingTrade.setStatus(trade.getStatus());
        return tradeRepository.save(existingTrade);
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

    public Trade getTradeByIdEntity(Long id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found with id: " + id));
    }

    public void deleteTrade(Long id) {
        tradeRepository.deleteById(id);
    }

    public Trade createTrade(TradeDTO tradeDTO, String initiatorUsername) {
        User initiator = userRepository.findByUsername(initiatorUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + initiatorUsername));

        ClothingItem item = clothingItemRepository.findById(tradeDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Clothing item not found with id: " + tradeDTO.getItemId()));

        if (item.getUser().equals(initiator)) {
            throw new RuntimeException("Cannot trade your own item.");
        }

        Trade trade = new Trade();
        trade.setStatus("Pending");
        trade.setItem(item);
        trade.setInitiator(initiator);
        trade.setReceiver(item.getUser());
        trade.setTradeDate(LocalDateTime.now());

        Trade savedTrade = tradeRepository.save(trade);

        //Notify the receiver
        notificationService.createNotification(
                savedTrade.getReceiver().getId(),
                "📦 New trade request! " + initiator.getUsername() + " wants to trade for your item: " + item.getTitle()
        );

        return savedTrade;
    }


    public Trade updateTradeStatus(Long id, String action, String username) {
        Trade trade = getTradeByIdEntity(id);

        if (!trade.getInitiator().getUsername().equals(username) &&
                !trade.getReceiver().getUsername().equals(username)) {
            throw new RuntimeException("User not authorized to update this trade.");
        }

        String notificationMessage;
        switch (action.toLowerCase()) {
            case "accept":
                if (trade.getReceiver().getUsername().equals(username)) {
                    trade.setStatus("Accepted");
                    notificationMessage = "✅ Trade accepted! Your trade request for " + trade.getItem().getTitle() + " was accepted.";
                    notificationService.createNotification(trade.getInitiator().getId(), notificationMessage);
                } else {
                    throw new RuntimeException("Only the receiver can accept trades.");
                }
                break;

            case "decline":
                if (trade.getReceiver().getUsername().equals(username)) {
                    trade.setStatus("Cancelled");
                    notificationMessage = "❌ Trade declined! Your trade request for " + trade.getItem().getTitle() + " was declined.";
                    notificationService.createNotification(trade.getInitiator().getId(), notificationMessage);
                } else {
                    throw new RuntimeException("Only the receiver can decline trades.");
                }
                break;

            case "complete":
                if (trade.getStatus().equals("Accepted")) {
                    trade.setStatus("Completed");
                    trade.getItem().setAvailable(false);
                    clothingItemRepository.save(trade.getItem());

                    notificationMessage = "🎉 Trade completed! The trade for " + trade.getItem().getTitle() + " has been finalized.";
                    notificationService.createNotification(trade.getInitiator().getId(), notificationMessage);
                    notificationService.createNotification(trade.getReceiver().getId(), notificationMessage);
                } else {
                    throw new RuntimeException("Only accepted trades can be completed.");
                }
                break;

            case "cancel":
                trade.setStatus("Cancelled");
                notificationMessage = "⚠️ Trade canceled! The trade for " + trade.getItem().getTitle() + " has been canceled.";
                notificationService.createNotification(trade.getInitiator().getId(), notificationMessage);
                notificationService.createNotification(trade.getReceiver().getId(), notificationMessage);
                break;

            default:
                throw new RuntimeException("Invalid action.");
        }

        return tradeRepository.save(trade);
    }


    public List<TradeDTO> generateTradeReport(String status, LocalDateTime startDate, LocalDateTime endDate) {
        return tradeRepository.findAll().stream()
                .filter(trade -> (status == null || trade.getStatus().equalsIgnoreCase(status)) &&
                        (startDate == null || !trade.getTradeDate().isBefore(startDate)) &&
                        (endDate == null || !trade.getTradeDate().isAfter(endDate)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TradeDTO> getTradesForUser(String username) {
        // Fetch the user entity by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Fetch trades where the user is either the initiator or receiver
        List<Trade> trades = tradeRepository.findTradesForUser(user);

        // Convert trades to DTOs
        return trades.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public TradeDTO convertToDTO(Trade trade) {
        return new TradeDTO(
                trade.getId(),
                trade.getStatus(),
                trade.getItem() != null ? trade.getItem().getId() : null,
                trade.getItem() != null ? trade.getItem().getTitle() : null,
                trade.getInitiator() != null ? trade.getInitiator().getId() : null,
                trade.getInitiator() != null ? trade.getInitiator().getUsername() : null,
                trade.getInitiator() != null ? trade.getInitiator().getEmail() : null, // Include email
                trade.getReceiver() != null ? trade.getReceiver().getId() : null,
                trade.getReceiver() != null ? trade.getReceiver().getUsername() : null,
                trade.getTradeDate()
        );
    }

    public List<Trade> findRecentByUser(User user) {
        return tradeRepository.findTop10ByUserOrderByTradeDateDesc(user);
    }
}
