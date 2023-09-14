package com.tms.a1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
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

    public String newUser(User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                String permitgroup = "admin";

                if (userRepo.checkgroup(username, permitgroup) != null) {
                    // User is in the group, continue with group creation logic
                    if (userRepo.existsByUsername(user.getUsername())) {
                        return "Duplicate";
                    }
                    String plainTextPassword = user.getPassword();
                    String hashedPassword = passwordEncoder.encode(plainTextPassword);
                    user.setPassword(hashedPassword);
                    userRepo.save(user);
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

    public String updateUser(User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                String permitgroup = "admin";

                if (userRepo.checkgroup(username, permitgroup) != null) {
                    // Retrieve the user by username.
                    Optional<User> optionalUser = userRepo.findByUsername(user.getUsername());

                    if (optionalUser.isPresent()) {
                        User existingUser = optionalUser.get();

                        // Update the user's information.
                        String plainTextPassword = user.getPassword();
                        String email = user.getEmail();
                        String groupToUpdate = user.getGroups();
                        int isActive = user.getIs_active();

                        // Hash the new password using BCrypt if provided and not empty.
                        if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
                            String hashedPassword = passwordEncoder.encode(plainTextPassword);
                            existingUser.setPassword(hashedPassword);
                        }

                        existingUser.setEmail(email);
                        existingUser.setGroups(groupToUpdate);
                        existingUser.setIs_active(isActive);

                        // Save the updated user back to the repository.
                        userRepo.save(existingUser);
                        return "Success";
                    } else {
                        return "User not found";
                    }
                } else {
                    return "You are unauthorized for this action";
                }
            } else {
                return "You are not an authenticated user";
            }
        } catch (Exception e) {
            // Handle different types of exceptions and return meaningful error messages
            if (e instanceof DataIntegrityViolationException) {
                return "Data integrity violation error occurred.";
            } else {
                return "An error occurred.";
            }
        }
    }

}
