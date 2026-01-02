package com.instagram.instagram_backend.service;

import com.instagram.instagram_backend.dto.PostRequest;
import com.instagram.instagram_backend.model.Post;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.repository.PostRepository;
import com.instagram.instagram_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        return List.of(post);
    }

    public List<Post> getPostsByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return postRepository.findByUser(user);

    }

    public List<Post> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return postRepository.findByUser(user);
    }

    @Transactional
    public Post createPost(PostRequest post, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Post newPost = new Post();
        newPost.setCaption(post.getCaption());
        newPost.setImageUrl(post.getImageUrl());
        newPost.setUser(user);
        return postRepository.save(newPost);
    }

    @Transactional
    public Post updatePost(Long id, PostRequest postRequest, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to update this post");
        }
        post.setCaption(postRequest.getCaption());
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        postRepository.deleteById(id);
        
    }
}
