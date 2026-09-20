package com.vehiclerental.controller;

import com.vehiclerental.dto.request.LoginRequest;
import com.vehiclerental.dto.request.RegisterRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.JwtResponse;
import com.vehiclerental.dto.response.UserResponse;
import com.vehiclerental.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        String message = (response.getRole() == com.vehiclerental.enums.Role.ROLE_OWNER)
                ? "Registration successful. Please wait for Admin approval before logging in."
                : "Registration successful. You can now login.";
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
