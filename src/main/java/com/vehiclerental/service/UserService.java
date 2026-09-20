package com.vehiclerental.service;

import com.vehiclerental.dto.response.UserResponse;
import com.vehiclerental.entity.User;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return new UserResponse(user);
    }
}
