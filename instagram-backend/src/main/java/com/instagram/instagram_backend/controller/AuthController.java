package com.instagram.instagram_backend.controller;

import com.instagram.instagram_backend.dto.AuthResponse;
import com.instagram.instagram_backend.dto.LoginRequest;
import com.instagram.instagram_backend.dto.RefreshTokenRequest;
import com.instagram.instagram_backend.dto.RegisterRequest;
import com.instagram.instagram_backend.model.RefreshToken;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.service.AuthService;
import com.instagram.instagram_backend.service.RefreshTokenService;
import com.instagram.instagram_backend.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private User user;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(authService.getAllUsers(), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/Register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return new ResponseEntity<>(user, HttpStatusCode.valueOf(201));
    }

    @PostMapping("/Login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            String accessToken = jwtUtil.generateAccessToken(loginRequest.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getUsername());
            refreshTokenService.CreateRefreshToken(refreshToken, loginRequest.getUsername(), jwtUtil.getREFRESH_TOKEN_VALIDITY());
            return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatusCode.valueOf(401));

        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        RefreshToken token = refreshTokenService.verifyExpiration(refreshToken);
        String username = token.getUser().getUsername();
        String newAccessToken = jwtUtil.generateAccessToken(username);

        return new ResponseEntity<>(new AuthResponse(newAccessToken, refreshToken), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteByToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity<>("User logged out successfully", HttpStatusCode.valueOf(200));
    }
}
