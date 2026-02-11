package com.instagram.instagram_backend.service;

import com.instagram.instagram_backend.exception.InvalidActionException;
import com.instagram.instagram_backend.exception.UserNotFoundException;
import com.instagram.instagram_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.instagram.instagram_backend.model.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void followUser(String followerUsername, String followingUsername) {
        if (followerUsername.equals(followingUsername)) {
            throw new InvalidActionException("Users cannot follow themselves");
        }
        User follower = userRepository.findByUsername(followerUsername);
        if (follower == null) {
            throw new UserNotFoundException("Follower user not found");
        }
        User following = userRepository.findByUsername(followingUsername);
        if (following == null) {
            throw new UserNotFoundException("Following user not found");
        }

        follower.getFollowing().add(following);
        following.getFollowers().add(follower);

        userRepository.save(follower);
        userRepository.save(following);
    }

    public void unfollowUser(String followerUsername, String followingUsername) {
        if (followerUsername.equals(followingUsername)) {
            throw new InvalidActionException("Users cannot unfollow themselves");
        }
        User follower = userRepository.findByUsername(followerUsername);
        if (follower == null) {
            throw new UserNotFoundException("Follower user not found");
        }
        User following = userRepository.findByUsername(followingUsername);
        if (following == null) {
            throw new UserNotFoundException("Following user not found");
        }

        follower.getFollowing().remove(following);
        following.getFollowers().remove(follower);

        userRepository.save(follower);
        userRepository.save(following);
    }

}
