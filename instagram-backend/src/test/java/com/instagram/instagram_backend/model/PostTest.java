package com.instagram.instagram_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    @Test
    public void testPostCreation() {
        Post post = new Post();
        post.setId(1L);
        post.setCaption("This is a test caption");
        post.setImageUrl("http://example.com/image.jpg");
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        post.setUser(user);
        assertEquals(1L, post.getId().longValue());
        assertEquals("This is a test caption", post.getCaption());
        assertEquals("http://example.com/image.jpg", post.getImageUrl());
        assertEquals("testuser", post.getUser().getUsername());
    }

}
