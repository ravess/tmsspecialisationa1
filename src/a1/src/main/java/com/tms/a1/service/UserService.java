package com.tms.a1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tms.a1.dao.UserDAO;
import com.tms.a1.entity.User;
import com.tms.a1.exception.EntityNotFoundException;

@Service
public class UserService {

 @Autowired
    private UserDAO userRepo;

    public User getUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                User user = userRepo.findByUsername(username);
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public User login(String Username){
        User user = userRepo.findByUsername(Username);

        if (user != null) {
            return user;
        } else {
            throw new EntityNotFoundException(Username, User.class);
        }
    }
    
}

