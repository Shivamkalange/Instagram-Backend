package com.instagram.instagram_backend.repository;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.instagram.instagram_backend.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
