package com.instagram.instagram_backend.dto;

import jakarta.persistence.Column;

public class AuthResponse {


    private String authToken;

    private String refreshToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AuthResponse() {
    }

    public AuthResponse(String authToken, String refreshToken) {
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }
}
