package com.instagram.instagram_backend.util;


import com.instagram.instagram_backend.dto.LoginRequest;
import com.instagram.instagram_backend.dto.RegisterRequest;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.model.role.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

//    @Value("${spring.security.jwt.expiration}")
//    private long expirationTime;

    @Value("${spring.security.jwt.ACCESS_TOKEN_EXPIRY}")
    private long ACCESS_TOKEN_VALIDITY;

    @Value("${spring.security.jwt.REFRESH_TOKEN_EXPIRY}")
    private long REFRESH_TOKEN_VALIDITY;

    public long getREFRESH_TOKEN_VALIDITY() {
        return REFRESH_TOKEN_VALIDITY;
    }

    public String generateAccessToken(LoginRequest user) {
        String username = user.getUsername();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String generateRefreshToken(LoginRequest user) {
        String username = user.getUsername();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }


//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .compact();
//    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
                ;
    }

    private boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
