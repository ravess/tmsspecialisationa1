package com.tms.a1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

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
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User requestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        try {
            String username = requestBody.getUsername();
            String plainTextPassword = requestBody.getPassword();
            // String email = requestBody.get("email");
            // String group = requestBody.get("groups");
            // int isActive = Integer.parseInt(requestBody.get("is_active"));
            if (userRepo.existsByUsername(username)) {
                resMsg = "Username already exists";
                response.put("msg", resMsg);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            String hashedPassword = passwordEncoder.encode(plainTextPassword);
            requestBody.setPassword(hashedPassword);

            // User newUser = new User();
            // newUser.setUsername(username);
            // newUser.setPassword(hashedPassword);
            // newUser.setEmail(email);
            // newUser.setGroups(group);
            // newUser.setIs_active(isActive);

            userRepo.save(requestBody);
            resMsg = "User has been successfully created";
            response.put("msg", resMsg);

            // Return a ResponseEntity with the saved user and a status code of 201
            // (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
            @Valid @RequestBody User requestBody, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        // Retrieve the user by username.
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update the user's information.
            String plainTextPassword = requestBody.getPassword();
            String email = requestBody.getEmail();
            String group = requestBody.getGroups();
            int isActive = requestBody.getIs_active();

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

            resMsg = "User has been successfully created";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // User not found with the given username.
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}
