package com.instagram.instagram_backend.repository;

import com.instagram.instagram_backend.model.RefreshToken;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    <Optional> RefreshToken findByToken(String token);

    void deleteByUser(String username);


}

