package com.instagram.instagram_backend.service;

import com.instagram.instagram_backend.dto.PostRequest;
import com.instagram.instagram_backend.model.Post;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.repository.PostRepository;
import com.instagram.instagram_backend.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "postsCache", key = "'allPosts'")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Cacheable(value = "postsCache", key = "#id")
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

    public List<Post> getFeedPosts(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        // For simplicity, returning all posts as feed
        return postRepository.findAll();
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

    public Page<PostRequest> getPostsWithPaginationAndSorting(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(post -> new PostRequest(post.getImageUrl(), post.getCaption()));
    }
}
