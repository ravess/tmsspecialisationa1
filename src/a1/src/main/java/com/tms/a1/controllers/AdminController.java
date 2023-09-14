package com.tms.a1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tms.a1.entity.Group;
import com.tms.a1.entity.User;
import com.tms.a1.repository.GroupRepo;
import com.tms.a1.repository.UserRepo;
import com.tms.a1.service.AdminService;

import jakarta.validation.Valid;

// @CrossOrigin(origins = "http://localhost:3000", maxAge = 1600, allowedHeaders = "*")
@RestController
public class AdminController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public String resMsg;
    public Map<String, Object> response = new HashMap<>();

    AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allusers = adminService.getAllUsers();
        if (allusers.isEmpty()) {
            return new ResponseEntity<>("No users found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allusers, HttpStatus.OK);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        List<Group> allgroups = groupRepo.findAll();

        if (allgroups.isEmpty()) {
            return new ResponseEntity<>("No groups found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allgroups, HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Object> getUser(@PathVariable String username) {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/newgroup")
    public ResponseEntity<?> addNewGroup(@RequestBody Map<String, String> requestBody) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                String group = "admin";

                if (userRepo.checkgroup(username, group) != null) {
                    // User is in the group, continue with group creation logic
                    String groupname = requestBody.get("group_name");

                    // Check if groupname already exists
                    if (groupRepo.existsByGroupName(groupname)) {
                        return ResponseEntity.badRequest().body("Duplicate group name");
                    }

                    Group newgroup = new Group();
                    newgroup.setGroupName(groupname);
                    groupRepo.save(newgroup);
                    resMsg = "New group " + groupname + " has been successfully created";
                    response.put("msg", resMsg);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    resMsg = "You are unauthorized for this action";
                    response.put("msg", resMsg);
                    // The user is not in the group, return unauthorized
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                resMsg = "You are not an authenticated user";
                response.put("msg", resMsg);
                // The user is not authenticated
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PostMapping("/newuser")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User requestBody, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        String resMsg;

        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                String group = "admin";
                if (userRepo.checkgroup(username, group) != null) {
                    String newUsername = requestBody.getUsername();
                    String plainTextPassword = requestBody.getPassword();
                    if (userRepo.existsByUsername(newUsername)) {
                        resMsg = "Username already exists";
                        response.put("msg", resMsg);
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                    }

                    String hashedPassword = passwordEncoder.encode(plainTextPassword);
                    requestBody.setPassword(hashedPassword);

                    userRepo.save(requestBody);

                    resMsg = "User has been successfully created";
                    response.put("msg", resMsg);

                    // Return a ResponseEntity with the saved user and a status code of 201
                    // (Created)
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    resMsg = "You are unauthorized for this action";
                    response.put("msg", resMsg);
                    // The user is not in the group, return unauthorized
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                resMsg = "You are not an authenticated user";
                response.put("msg", resMsg);
                // The user is not authenticated
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle other errors
            resMsg = "An error occurred creating user";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUserByUsername(
            @PathVariable String username,
            @Valid @RequestBody User requestBody) {
        Map<String, Object> response = new HashMap<>();
        String resMsg;

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String tokenName = authentication.getName();
                String tokenGroup = "admin";

                // Check if the authenticated user is in the "admin" group
                if (userRepo.checkgroup(tokenName, tokenGroup) != null) {
                    // Retrieve the user by username.
                    Optional<User> optionalUser = userRepo.findByUsername(username);

                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        // Update the user's information.
                        String plainTextPassword = requestBody.getPassword();
                        String email = requestBody.getEmail();
                        String groupToUpdate = requestBody.getGroups();
                        int isActive = requestBody.getIs_active();

                        // Hash the new password using BCrypt if provided and not empty.
                        if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
                            String hashedPassword = passwordEncoder.encode(plainTextPassword);
                            requestBody.setPassword(hashedPassword);
                        }

                        user.setEmail(email);

                        user.setGroups(groupToUpdate);
                        user.setIs_active(isActive);

                        // Save the updated user back to the repository.
                        userRepo.save(user);

            resMsg = "User has been successfully updated";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // User not found with the given username.
            resMsg = "User not found";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
