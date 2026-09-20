package com.vehiclerental.service;

import com.vehiclerental.config.JwtUtils;
import com.vehiclerental.config.UserPrincipal;
import com.vehiclerental.dto.request.LoginRequest;
import com.vehiclerental.dto.request.RegisterRequest;
import com.vehiclerental.dto.response.JwtResponse;
import com.vehiclerental.dto.response.UserResponse;
import com.vehiclerental.entity.User;
import com.vehiclerental.enums.Role;
import com.vehiclerental.enums.UserStatus;
import com.vehiclerental.exception.DuplicateResourceException;
import com.vehiclerental.exception.UnauthorizedException;
import com.vehiclerental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }

        if (request.getRole() == Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Admin registration is not allowed via public endpoint");
        }

        // Owners require Admin approval before getting ACTIVE status
        UserStatus initialStatus = (request.getRole() == Role.ROLE_OWNER) 
                ? UserStatus.PENDING 
                : UserStatus.APPROVED;

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone(),
                request.getRole(),
                initialStatus
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (user.getStatus() == UserStatus.PENDING) {
            throw new UnauthorizedException("Your owner account is currently pending Admin approval.");
        }

        if (user.getStatus() == UserStatus.REJECTED) {
            throw new UnauthorizedException("Your registration request was rejected by Admin.");
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new UnauthorizedException("Your account has been suspended by Admin.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return new JwtResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getUser().getName(),
                userPrincipal.getUser().getEmail(),
                userPrincipal.getUser().getRole(),
                userPrincipal.getUser().getStatus()
        );
    }
}
