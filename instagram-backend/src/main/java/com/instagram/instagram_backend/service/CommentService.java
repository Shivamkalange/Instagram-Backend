package com.instagram.instagram_backend.service;


import com.instagram.instagram_backend.dto.CommentRequest;
import com.instagram.instagram_backend.model.Comment;
import com.instagram.instagram_backend.model.Post;
import com.instagram.instagram_backend.model.User;
import com.instagram.instagram_backend.repository.CommentRepository;
import com.instagram.instagram_backend.repository.PostRepository;
import com.instagram.instagram_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Comment> getCommentsByPostId(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    public List<Comment> getCommentsByUsernameAndPostId(String username, Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("User is not the owner of the post");
        }
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }


    public Comment addComment(String username, Long postId, CommentRequest comment) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setPost(post);
        newComment.setUser(user);
        return commentRepository.save(newComment);
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentRequest commentRequest) {
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment == null) {
            throw new RuntimeException("Comment not found");
        }
        existingComment.setContent(commentRequest.getContent());
        return commentRepository.save(existingComment);
    }

    @Transactional
    public void deleteComment(Long commentID) {
        Comment exisitingComment = commentRepository.findById(commentID).orElse(null);
        if (exisitingComment == null) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.delete(exisitingComment);
    }
}
