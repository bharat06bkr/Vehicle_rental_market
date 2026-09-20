package com.vehiclerental.config;

import com.vehiclerental.entity.User;
import com.vehiclerental.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getUser();
        }
        throw new UnauthorizedException("User is not authenticated");
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
