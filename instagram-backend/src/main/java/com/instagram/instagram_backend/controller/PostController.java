package com.instagram.instagram_backend.controller;

import com.instagram.instagram_backend.dto.PostRequest;
import com.instagram.instagram_backend.model.Post;
import com.instagram.instagram_backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@EnableWebSecurity
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("PaginationAndSorting/{page}/{size}")
    public ResponseEntity<Page<PostRequest>> getPostsWithPaginationAndSorting(@PathVariable int page, @PathVariable int size, @PathVariable Optional<String> sortBy) {
        Page<PostRequest> posts = postService.getPostsWithPaginationAndSorting(page, size, sortBy.orElse("caption"));
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            List<Post> post = postService.getPostsById(id);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);


        }
    }

    @GetMapping("user/{username}")
    public ResponseEntity<?> getPostsByUsername(@PathVariable String username) {
        try {
            List<Post> userPosts = postService.getPostsByUsername(username);
            return new ResponseEntity<>(userPosts, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("userId/{id}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long id, Authentication authentication) {

        String username = authentication.getName();
        List<Post> userPosts = postService.getPostsByUserId(id);
        if (userPosts.isEmpty()) {
            return new ResponseEntity<>("No posts found for this user", HttpStatus.NOT_FOUND);
        }
        if (!userPosts.get(0).getUser().getUsername().equals(username)) {
            return new ResponseEntity<>("Unauthorized access to posts", HttpStatus.UNAUTHORIZED);
        }
        userPosts = postService.getPostsByUserId(id);
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }


    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeedPosts(Authentication authentication) {
        String username = authentication.getName();
        List<Post> feedPosts = postService.getFeedPosts(username);
        return ResponseEntity.ok(feedPosts);
    }


    @PostMapping("{username}")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, @PathVariable String username) {
        try {
            Post newPost = postService.createPost(postRequest, username);
            return new ResponseEntity<>(newPost, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{username}/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, @PathVariable String username) {
        try {
            Post updatedPost = postService.updatePost(id, postRequest, username);
            return new ResponseEntity<>(updatedPost, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{username}/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @PathVariable String username) {
        try {
            postService.deletePost(id, username);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
