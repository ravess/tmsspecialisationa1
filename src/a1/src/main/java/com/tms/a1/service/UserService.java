package com.tms.a1.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserService {

    public String getUserFromToken(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                return username;
            } else {
                return "You are not an authenticated user";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "An error occurred.";
        }
    }

    
}
