package com.tms.a1.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.a1.entity.Group;
import com.tms.a1.entity.User;
import com.tms.a1.repository.GroupRepo;
import com.tms.a1.repository.UserRepo;

@RestController
public class AdminController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allusers = userRepo.findAll();
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
        String groupname = requestBody.get("group_name");
        // check if groupname already exists
        if (groupRepo.existsByGroupName(groupname)) {
            return new ResponseEntity<>("Duplicate group name", HttpStatus.BAD_REQUEST);
        }

        Group newgroup = new Group();
        newgroup.setGroupName(groupname);
        return new ResponseEntity<>(groupRepo.save(newgroup), HttpStatus.CREATED);
    }

    @PostMapping("/newuser")
    public ResponseEntity<User> addNewUser(@RequestBody User requestBody) {
        // String username = requestBody.get("username");
         String plainTextPassword = requestBody.getPassword();
        // String email = requestBody.get("email");
        // String group = requestBody.get("groups");
        // int isActive = Integer.parseInt(requestBody.get("is_active"));

        String hashedPassword = passwordEncoder.encode(plainTextPassword);
        requestBody.setPassword(hashedPassword);
        
        //User newUser = new User();
        // newUser.setUsername(username);
        // newUser.setPassword(hashedPassword);
        // newUser.setEmail(email);
        // newUser.setGroups(group);
        // newUser.setIs_active(isActive);

        User savedUser = userRepo.save(requestBody);

        // Return a ResponseEntity with the saved user and a status code of 201
        // (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUserByUsername(
            @PathVariable String username,
            @RequestBody Map<String, String> requestBody) {

        // Retrieve the user by username.
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update the user's information.
            String plainTextPassword = requestBody.get("password");
            String email = requestBody.get("email");
            String group = requestBody.get("groups");
            int isActive = Integer.parseInt(requestBody.get("is_active"));

            // Hash the new password using BCrypt if provided or if it's an empty string.
            if (plainTextPassword != null) {
                String hashedPassword = plainTextPassword.isEmpty() ? user.getPassword()
                        : passwordEncoder.encode(plainTextPassword);
                user.setPassword(hashedPassword);
            }

            // Check if email is provided and not empty or null before updating.
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }

            user.setGroups(group);
            user.setIs_active(isActive);

            // Save the updated user back to the repository.
            userRepo.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            // User not found with the given username.
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}
