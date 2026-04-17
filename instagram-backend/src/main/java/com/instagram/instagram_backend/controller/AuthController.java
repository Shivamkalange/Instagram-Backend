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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration, login, token refresh and logout")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("all")
    @Operation(summary = "Get all users", description = "Returns a list of all registered users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    })
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(authService.getAllUsers(), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Create a new user account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    public ResponseEntity<?> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return new ResponseEntity<>(user, HttpStatusCode.valueOf(201));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate and return access + refresh tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<?> loginUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            String accessToken = jwtUtil.generateAccessToken(loginRequest);
            String refreshToken = jwtUtil.generateRefreshToken(loginRequest);
            refreshTokenService.createRefreshToken(refreshToken, loginRequest.getUsername(), jwtUtil.getREFRESH_TOKEN_VALIDITY());
            return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatusCode.valueOf(401));
        }
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Provide a refresh token to receive a new access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New access token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token", content = @Content)
    })
    public ResponseEntity<?> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
            )
            @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        RefreshToken token = refreshTokenService.verifyExpiration(refreshToken);
        LoginRequest loginRequest = new LoginRequest(token.getUser().getUsername(), null, token.getUser().getRole());
        String newAccessToken = jwtUtil.generateAccessToken(loginRequest);

        return new ResponseEntity<>(new AuthResponse(newAccessToken, refreshToken), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidate the given refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged out successfully",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> logoutUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token to invalidate",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
            )
            @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteByToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity<>("User logged out successfully", HttpStatusCode.valueOf(200));
    }
}
