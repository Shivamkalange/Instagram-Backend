package com.instagram.instagram_backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class CommentRequest {

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String content;

    public @NotBlank @Size(max = 500) String getContent() {
        return content;
    }

    public void setContent(@NotBlank @Size(max = 500) String content) {
        this.content = content;
    }

    public CommentRequest(String content) {
        this.content = content;
    }

    public CommentRequest() {
    }
}
