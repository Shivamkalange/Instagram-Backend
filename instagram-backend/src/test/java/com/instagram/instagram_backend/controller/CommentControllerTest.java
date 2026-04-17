package com.instagram.instagram_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagram.instagram_backend.dto.CommentRequest;
import com.instagram.instagram_backend.model.Comment;
import com.instagram.instagram_backend.service.CommentService;
import com.instagram.instagram_backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

// ---------------- GET /{id} ----------------

    @Test
    void getCommentsByPostId_success() throws Exception {
        when(commentService.getCommentsByPostId(1L))
                .thenReturn(List.of(new Comment()));

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCommentsByPostId_empty() throws Exception {
        when(commentService.getCommentsByPostId(1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCommentsByPostId_exception() throws Exception {
        when(commentService.getCommentsByPostId(1L))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isInternalServerError());
    }

// ---------------- GET /{username}/{postId} ----------------

    @Test
    void getCommentsByUsernameAndPostId_success() throws Exception {
        when(commentService.getCommentsByUsernameAndPostId("john", 1L))
                .thenReturn(List.of(new Comment()));

        mockMvc.perform(get("/api/comments/john/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCommentsByUsernameAndPostId_empty() throws Exception {
        when(commentService.getCommentsByUsernameAndPostId("john", 1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/comments/john/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCommentsByUsernameAndPostId_exception() throws Exception {
        when(commentService.getCommentsByUsernameAndPostId("john", 1L))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/comments/john/1"))
                .andExpect(status().isInternalServerError());
    }

// ---------------- POST ----------------

    @Test
    void addComment_success() throws Exception {
        CommentRequest request = new CommentRequest();
        Comment comment = new Comment();

        when(commentService.addComment("john", 1L, request))
                .thenReturn(comment);

        mockMvc.perform(post("/api/comments/john/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void addComment_exception() throws Exception {
        CommentRequest request = new CommentRequest();

        when(commentService.addComment("john", 1L, request))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/comments/john/1")
                        .contentType(MediaType.APPLICATION_JSON))
                       // .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

// ---------------- PUT ----------------

    @Test
    void updateComment_success() throws Exception {
        CommentRequest request = new CommentRequest();
        Comment comment = new Comment();

        when(commentService.updateComment(1L, request))
                .thenReturn(comment);

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateComment_exception() throws Exception {
        CommentRequest request = new CommentRequest();

        when(commentService.updateComment(1L, request))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                       // .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

// ---------------- DELETE ----------------

    @Test
    void deleteComment_success() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteComment_exception() throws Exception {
        doThrow(new RuntimeException()).when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isInternalServerError());
    }


}
