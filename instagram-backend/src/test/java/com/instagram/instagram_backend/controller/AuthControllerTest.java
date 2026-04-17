//package com.instagram.instagram_backend.controller;
//
//import com.instagram.instagram_backend.dto.LoginRequest;
//import com.instagram.instagram_backend.dto.RegisterRequest;
//import com.instagram.instagram_backend.model.User;
//import com.instagram.instagram_backend.service.AuthService;
//import com.instagram.instagram_backend.service.RefreshTokenService;
//import com.instagram.instagram_backend.util.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthControllerTest {
//
//    @Mock
//    private AuthService authService;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private AuthController authController;
//
//    @Test
//    public void testRegisterUser() {
//        User user = new User();
//        user.setUsername("testuser");
//        user.setEmail("testuser@gmail.com");
//        user.setPassword("password123");
//
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setUsername("testuser");
//        registerRequest.setEmail("testuser@gmail.com");
//        registerRequest.setPassword("password123");
//
//        when(authService.register(any(RegisterRequest.class))).thenReturn(user);
//
//        ResponseEntity<?> response = authController.registerUser(registerRequest);
//
//        //assertEquals(201, response.getStatusCode());
//        assertEquals(user, response.getBody());
//        verify(authService).register(any(RegisterRequest.class));
//    }
//
//    @Test
//    public void testLoginUser(){
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password123");
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("password123");
//
//       when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
//       when(jwtUtil.generateAccessToken(any(LoginRequest.class))).thenReturn("accessToken");
//       when(jwtUtil.generateRefreshToken(any(LoginRequest.class))).thenReturn("refreshToken");
//
//         assertEquals("accessToken", authController.loginUser(loginRequest).getBody().());
//       assertEquals("refreshToken", authController.loginUser(loginRequest).getBody().getRefreshToken());
//       assertEquals(201, authController.loginUser(loginRequest).getStatusCodeValue());
//
//    }
//
//
//
//
//
//
//
//
//}
