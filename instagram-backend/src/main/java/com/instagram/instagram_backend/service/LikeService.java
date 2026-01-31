package com.instagram.instagram_backend.service;


import com.instagram.instagram_backend.model.Like;
import com.instagram.instagram_backend.model.Post;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.repository.LikeRepository;
import com.instagram.instagram_backend.repository.PostRepository;
import com.instagram.instagram_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        return likeRepository.countByPost(post);
    }

    public boolean likePost(String username, Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false; // Post unliked
        }
        else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            return true; // Post liked
        }
    }

}
