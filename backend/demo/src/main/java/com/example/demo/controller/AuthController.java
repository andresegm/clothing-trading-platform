package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        // Ensure password is hashed before saving
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

        authService.registerUser(user);

        // Return JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Authenticate user and generate tokens
            Map<String, Object> authResponse = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            String accessToken = (String) authResponse.get("token");
            String refreshToken = (String) authResponse.get("refreshToken");
            System.out.println("Generated Access Token: " + accessToken);
            System.out.println("Generated Refresh Token: " + refreshToken);
            // Set cookies
            addCookie(response, "accessToken", accessToken, 900); // 15 mins
            addCookie(response, "refreshToken", refreshToken, 604800); // 7 days

            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extract refresh token from cookies
            String refreshToken = extractCookie(request, "refreshToken");

            if (refreshToken == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Refresh token missing"));
            }

            // Generate new access token
            String newAccessToken = authService.refreshAccessToken(refreshToken);

            // Set new access token cookie
            addCookie(response, "accessToken", newAccessToken, 900);

            return ResponseEntity.ok(Map.of("message", "Token refreshed"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // Helper method to add cookies securely
    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/"); // Available on all endpoints
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // Helper method to extract cookie from request
    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        removeCookie(response, "accessToken");
        removeCookie(response, "refreshToken");

        System.out.println("User logged out. Cookies cleared.");

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    private void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
