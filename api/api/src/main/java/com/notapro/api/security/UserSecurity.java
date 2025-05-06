package com.notapro.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.notapro.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    
    private final UserRepository userRepository;
    
    public boolean isUserSelf(Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String userEmail = authentication.getName();
        
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(userEmail))
                .orElse(false);
    }
}