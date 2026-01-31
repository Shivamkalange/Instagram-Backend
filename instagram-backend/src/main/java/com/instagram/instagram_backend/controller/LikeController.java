package com.instagram.instagram_backend.controller;


import com.instagram.instagram_backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("{postId}/count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        try {
            Long likeCount = likeService.getLikeCount(postId);
            return new ResponseEntity<>(likeCount, HttpStatusCode.valueOf(200));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("{username}/{postId}")
    public ResponseEntity<?> likePost(@PathVariable String username, @PathVariable Long postId) {
        try {
            boolean liked = likeService.likePost(username, postId);
            Map<String, Object> response = new HashMap<>();
            if (liked) {
                response.put("message", "Post liked successfully");
            } else {
                response.put("message", "Post unliked successfully");
            }
            response.put("liked", liked);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);

        }
    }

}
