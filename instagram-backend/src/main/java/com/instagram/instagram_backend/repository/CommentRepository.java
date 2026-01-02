package com.instagram.instagram_backend.repository;

import com.instagram.instagram_backend.model.Comment;
import com.instagram.instagram_backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostOrderByCreatedAtDesc(Post post);

}
