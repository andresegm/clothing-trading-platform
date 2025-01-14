package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

        authService.registerUser(user);

        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (isAuthenticated) {
            // Fetch the user by username
            User user = userService.findByUsername(loginRequest.getUsername());

            // Return the token and userId as a JSON response
            Map<String, String> response = new HashMap<>();
            response.put("token", "dummy-jwt-token-for-" + loginRequest.getUsername());
            response.put("userId", String.valueOf(user.getId())); // Add userId to the response

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
    }


}
