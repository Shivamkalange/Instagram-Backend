package com.instagram.instagram_backend.service;

import com.instagram.instagram_backend.model.RefreshToken;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.repository.RefreshTokenRepository;
import com.instagram.instagram_backend.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken CreateRefreshToken(String token, String username, Long validityInMs) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(validityInMs));
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token not found: " + token);
        }
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token has expired. Please login again.");
        }
        return refreshToken;
    }

    public void deleteByToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken);
        if (token == null) {
            throw new IllegalArgumentException("Refresh token not found: " + refreshToken);
        } else {
            refreshTokenRepository.delete(token);
        }
    }


}
