package com.kimhok.tickets.common.utils;

import com.kimhok.tickets.config.security.CustomUserDetails;
import com.kimhok.tickets.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails){
            return userDetails.getUser();
        }
        return null;
    }
    public String getCurrentUserId(){
        User userId = getCurrentUser();
        return (userId != null) ? userId.getId() : null;
    }

}
