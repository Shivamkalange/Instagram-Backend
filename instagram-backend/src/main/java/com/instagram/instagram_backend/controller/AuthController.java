package com.instagram.instagram_backend.controller;

import com.instagram.instagram_backend.dto.LoginRequest;
import com.instagram.instagram_backend.dto.RegisterRequest;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.service.AuthService;
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

    @Autowired
    private JwtUtil jwtUtil;

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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return new ResponseEntity<>(token, HttpStatusCode.valueOf(200));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }



}
