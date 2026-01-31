package com.instagram.instagram_backend.repository;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.instagram.instagram_backend.model.Like;
import com.instagram.instagram_backend.model.Post;
import com.instagram.instagram_backend.model.User;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByPost(Post post);

    Optional<Like> findByUserAndPost(User user, Post post);
}
