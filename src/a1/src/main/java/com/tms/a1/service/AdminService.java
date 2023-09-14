package com.tms.a1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tms.a1.entity.Group;
import com.tms.a1.entity.User;
import com.tms.a1.exception.EntityNotFoundException;
import com.tms.a1.repository.GroupRepo;
import com.tms.a1.repository.UserRepo;

@Service
public class AdminService {
    
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupRepo groupRepo;
    public Map<String, Object> response = new HashMap<>();

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public List<Group> getAllGroups() {
        return groupRepo.findAll();
    }

    public User getUser(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException(username, User.class);
        }
    }

    public String newGroup(Group group){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                String permitgroup = "admin";
                
                if (userRepo.checkgroup(username, permitgroup) != null) {
                    // User is in the group, continue with group creation logic
                    if (groupRepo.existsByGroupName(group.getGroupName())) {
                        return "Duplicate";
                    }
                    groupRepo.save(group);
                    return "Success";
                } else {
                    return "You are unauthorized for this action";
                }
            } else {
                return "You are not an authenticated user";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "An error occurred.";
        }
    }
}
