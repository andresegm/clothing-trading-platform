package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.refresh-secret}")
    private String REFRESH_SECRET_KEY;

    private final Map<String, String> refreshTokenStore = new HashMap<>();

    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        // Assign default role
        UserRole defaultRole = userRoleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found."));
        user.setRoles(Set.of(defaultRole));

        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public Map<String, Object> authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT Tokens
        String accessToken = generateToken(user, SECRET_KEY, 900000); // 15 mins
        String refreshToken = generateToken(user, REFRESH_SECRET_KEY, 604800000); // 7 days

        // Store refresh token
        refreshTokenStore.put(refreshToken, username);

        // Return only user info (tokens are now in cookies)
        return Map.of(
                "message", "Authentication successful",
                "userId", user.getId(),
                "token", accessToken,
                "refreshToken", refreshToken
        );

    }


    public String generateToken(User user, String secret, long expirationMs) {


        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }


    public String refreshAccessToken(String refreshToken) {
        if (!refreshTokenStore.containsKey(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = refreshTokenStore.get(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateToken(user, SECRET_KEY, 86400000); // New access token (24h)
    }
}
