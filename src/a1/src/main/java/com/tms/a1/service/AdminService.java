package com.tms.a1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tms.a1.dao.GroupDAO;
import com.tms.a1.dao.UserDAO;
import com.tms.a1.entity.Group;
import com.tms.a1.entity.User;
import com.tms.a1.exception.EntityNotFoundException;

@Service
public class AdminService {

    @Autowired
    private UserDAO userRepo;
    @Autowired
    private GroupDAO groupRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public List<Group> getAllGroups() {
    return groupRepo.findAll();
    }

    public User getUser(String username) {
        User user = userRepo.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new EntityNotFoundException(username, User.class);
        }
    }

    public String createGroup(Group group){
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

                System.out.println(user);

                if (userRepo.checkgroup(username, permitgroup) != null) {
                    // User is in the group, continue with group creation logic
                    if (userRepo.existByUsername(user.getUsername())) {
                        return "Duplicate";
                    }
                    String plainTextPassword = user.getPassword();
                    String hashedPassword = passwordEncoder.encode(plainTextPassword);
                    user.setPassword(hashedPassword);
                    userRepo.saveUser(user);
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

    // public String updateUser(String username, User user) {
    // try {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // if (authentication != null && authentication.isAuthenticated()) {
    // String tokenName = authentication.getName();
    // String permitgroup = "admin";

    // if (userRepo.checkgroup(tokenName, permitgroup) != null) {
    // // Retrieve the user by username.
    // Optional<User> optionalUser = userRepo.findByUsername(username);

    // if (optionalUser.isPresent()) {
    // User existingUser = optionalUser.get();
    // if (user.getPassword() == null || user.getPassword() == "") {
    // user.setPassword(existingUser.getPassword());
    // }

    // // Update the user's information.
    // String plainTextPassword = user.getPassword();
    // String email = user.getEmail();
    // String groupToUpdate = user.getGroups();
    // int isActive = user.getIsActive();

    // Hash the new password using BCrypt if provided and not empty.
    // if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
    // if (!isPasswordValid(plainTextPassword)) {
    // return "Invalid password";
    // }
    // String hashedPassword = passwordEncoder.encode(plainTextPassword);
    // existingUser.setPassword(hashedPassword);
    // }

    // if (email != null && !email.isEmpty()) {
    // if (!isValidEmail(email)) {
    // return "Invalid email";
    // }
    // existingUser.setEmail(email);
    // }

    // existingUser.setGroups(groupToUpdate);
    // existingUser.setIsActive(isActive);

    // // Save the updated user back to the repository.
    // userRepo.save(existingUser);

    // return "Success";
    // } else {
    // return "User not found";
    // }
    // } else {
    // return "You are unauthorized for this action";
    // }
    // } else {
    // return "You are not an authenticated user";
    // }
    // } catch (Exception e) {
    // // Handle different types of exceptions and return meaningful error messages
    // if (e instanceof DataIntegrityViolationException) {
    // return "Data integrity violation error occurred.";
    // } else {
    // return "An error occurred.";
    // }
    // }
    // }
    public String updateUser(String username, User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String permitgroup = "admin";
    
                // Check if the authenticated user has permission to perform this action
                if (userRepo.checkgroup(authentication.getName(), permitgroup) != null) {
                    // Retrieve the user by username
                    User existingUser = userRepo.findByUsername(username);
    
                    if (existingUser != null) {
                        // Update the user's information
                        String plainTextPassword = user.getPassword();
                        String email = user.getEmail();
                        String groupToUpdate = user.getGroups();
                        int isActive = user.getIsActive();
    
                        // Hash the new password using BCrypt if provided and not empty
                        if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
                            if (!isPasswordValid(plainTextPassword)) {
                                return "Invalid password";
                            }
                            String hashedPassword = passwordEncoder.encode(plainTextPassword);
                            existingUser.setPassword(hashedPassword);
                        }
    
                        if (email != null && !email.isEmpty()) {
                            if (!isValidEmail(email)) {
                                return "Invalid email";
                            }
                            existingUser.setEmail(email);
                        }
    
                        existingUser.setGroups(groupToUpdate);
                        existingUser.setIsActive(isActive);
    
                        // Save the updated user back to the repository
                        userRepo.saveUser(existingUser);
    
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
            e.printStackTrace(); // Log the exception stack trace
            return "An error occurred: " + e.getMessage(); // Return the exception message
        }
    }
    
    // Custom validation logic for password
    private boolean isPasswordValid(String password) {
        // Check if the password contains at least one alphabet character, one number,
        // and one special character.
        boolean hasAlphabet = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasAlphabet = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecialCharacter = true;
            }

            // If all required conditions are met, return true.
            if (hasAlphabet && hasNumber && hasSpecialCharacter) {
                return true;
            }
        }

        // If any of the required conditions are not met, return false.
        return false;
    }

    // custom validation for email check (if email input is not null/not empty
    // string)
    private boolean isValidEmail(String email) {
        // Regular expression pattern for a valid email address
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        return email.matches(emailPattern);
    }

}
