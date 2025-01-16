package com.example.demo.controller;

import com.example.demo.dto.ClothingItemDTO;
import com.example.demo.dto.TradeDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.service.ClothingItemService;
import com.example.demo.service.TradeService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserService userService;
    private final ClothingItemService clothingItemService;
    private final TradeService tradeService;

    // Constructor injection for services
    public DashboardController(UserService userService, ClothingItemService clothingItemService, TradeService tradeService) {
        this.userService = userService;
        this.clothingItemService = clothingItemService;
        this.tradeService = tradeService;
    }

    @GetMapping("/data")
    public ResponseEntity<?> getDashboardData(Principal principal) {
        if (principal == null) {
            System.out.println("No principal found. Token might be invalid.");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        System.out.println("Authenticated principal: " + principal.getName());
        String username = principal.getName();

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }


        // Fetch recent clothing items
        List<ClothingItemDTO> recentItems = clothingItemService.findRecentByUser(user).stream()
                .map(item -> new ClothingItemDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getSize(),
                        item.getBrand(),
                        item.getCondition(),
                        item.getPrice(),
                        item.getUser().getId(),
                        item.isAvailable()
                ))
                .toList();

        // Fetch recent trades
        List<TradeDTO> recentTrades = tradeService.findRecentByUser(user).stream()
                .map(trade -> new TradeDTO(
                        trade.getId(),
                        trade.getStatus(),
                        trade.getItem().getId(),
                        trade.getItem().getTitle(),
                        trade.getInitiator().getId(),
                        trade.getInitiator().getUsername(),
                        trade.getInitiator().getEmail(),
                        trade.getReceiver().getId(),
                        trade.getReceiver().getUsername()
                ))
                .toList();


        // Create the response
        Map<String, Object> response = new HashMap<>();
        response.put("user", new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
        response.put("recentClothingItems", recentItems);
        response.put("recentTrades", recentTrades);

        return ResponseEntity.ok(response);
    }

}
