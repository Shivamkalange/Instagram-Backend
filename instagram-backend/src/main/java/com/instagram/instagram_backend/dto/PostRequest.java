package com.instagram.instagram_backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PostRequest {

    @NotBlank
    @Size(max=2200)
    @Column(nullable = false,length = 2200)
    private String imageUrl;

    @Size(max=1000)
    private String caption;

    public @NotBlank @Size(max = 2200) String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NotBlank @Size(max = 2200) String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @Size(max = 1000) String getCaption() {
        return caption;
    }

    public void setCaption(@Size(max = 1000) String caption) {
        this.caption = caption;
    }

    public PostRequest(String imageUrl, String caption) {
        this.imageUrl = imageUrl;
        this.caption = caption;
    }
    public PostRequest() {
    }

    @Override
    public String toString() {
        return "PostRequest{" +
                "imageUrl='" + imageUrl + '\'' +
                ", caption='" + caption + '\'' +
                '}';
    }
}
