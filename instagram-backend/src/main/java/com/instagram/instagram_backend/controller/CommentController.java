package com.instagram.instagram_backend.controller;


import com.instagram.instagram_backend.dto.CommentRequest;
import com.instagram.instagram_backend.model.Comment;
import com.instagram.instagram_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("{id}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long id) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(id);
            if (comments.isEmpty()) {
                return new ResponseEntity<>("No comments found for this post", HttpStatusCode.valueOf(204));
            } else {
                return new ResponseEntity<>(comments, HttpStatusCode.valueOf(200));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("{username}/{postId}")
    public ResponseEntity<?> getCommentsByUsernameAndPostId(@PathVariable String username, @PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByUsernameAndPostId(username, postId);
            if (comments.isEmpty()) {
                return new ResponseEntity<>("No comments found for this post by the specified user", HttpStatusCode.valueOf(204));
            } else {
                return new ResponseEntity<>(comments, HttpStatusCode.valueOf(200));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("{username}/{postId}")
    public ResponseEntity<?> addComment(@PathVariable String username, @PathVariable Long postId, @RequestBody CommentRequest comment) {
        // Implementation for adding a comment goes here
        try {
            Comment comment1 = commentService.addComment(username, postId, comment);
            return new ResponseEntity<>(comment1, HttpStatusCode.valueOf(201));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        try {
            Comment updatedComment = commentService.updateComment(commentId, commentRequest);
            return new ResponseEntity<>(updatedComment, HttpStatusCode.valueOf(201));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>("Comment deleted successfully", HttpStatusCode.valueOf(200));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


}
