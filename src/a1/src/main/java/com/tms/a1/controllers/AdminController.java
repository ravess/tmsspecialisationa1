package com.tms.a1.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("/newgroup")
    public ResponseEntity<?> addNewGroup(@RequestBody Map<String, String> requestBody){
        String groupname = requestBody.get("group_name");
        //check if groupname already exists
        if(groupRepo.existsByGroupName(groupname)){
            return new ResponseEntity<>("Duplicate group name", HttpStatus.BAD_REQUEST);
        }
        //else
        Group newgroup = new Group();
        newgroup.setGroupName(groupname);
        return new ResponseEntity<>(groupRepo.save(newgroup), HttpStatus.CREATED);
    }

    @PostMapping("/newuser")
    public User addNewUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String plainTextPassword = requestBody.get("password"); // Get the plain text password
        String email = requestBody.get("email");
        String group = requestBody.get("groups");
        int isActive = Integer.parseInt(requestBody.get("is_active"));

        // Hash the password using BCrypt
        String hashedPassword = passwordEncoder.encode(plainTextPassword);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword); // Set the hashed password
        newUser.setEmail(email);
        newUser.setGroups(group);
        newUser.setIs_active(isActive);
        userRepo.save(newUser);
        return newUser;
    }
}
