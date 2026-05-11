package com.instagram.instagram_backend.service;

import com.instagram.instagram_backend.model.RefreshToken;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.repository.RefreshTokenRepository;
import com.instagram.instagram_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public void createOrUpdateRefreshToken(String token, String username, Long validityInMs) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);
        if (existingToken.isPresent()) {
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setToken(token);
            refreshToken.setExpiryDate(Instant.now().plusMillis(validityInMs));
             refreshTokenRepository.save(refreshToken);
        } else {

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(token);
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plusMillis(validityInMs));
             refreshTokenRepository.save(refreshToken);
        }
    }

    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found: " + token));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token has expired. Please login again.");
        }
        return refreshToken;
    }

    public void deleteByToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found: " + refreshToken));
        refreshTokenRepository.delete(token);
    }


}
