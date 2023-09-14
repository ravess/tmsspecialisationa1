package com.tms.a1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.tms.a1.repository.UserRepo;
import com.tms.a1.service.AdminService;

import jakarta.validation.Valid;

// @CrossOrigin(origins = "http://localhost:3000", maxAge = 1600, allowedHeaders = "*")
@RestController
public class AdminController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public String resMsg;
    public Map<String, Object> response = new HashMap<>();

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allusers = adminService.getAllUsers();
        if (allusers.isEmpty()) {
            resMsg = "No Users Found.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allusers, HttpStatus.OK);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        List<Group> allgroups = adminService.getAllGroups();
        if (allgroups.isEmpty()) {
            resMsg = "No Groups Found.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allgroups, HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Object> getUser(@PathVariable String username) {
        User user = adminService.getUser(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/newgroup")
    public ResponseEntity<?> addNewGroup(@RequestBody Group requestBodyGroup) {
        String res = adminService.newGroup(requestBodyGroup);
        if (res.equals("Success")) {
            resMsg = "New Group " + requestBodyGroup.getGroupName() + " Created.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (res.equals("Duplicate")) {
            resMsg = "Duplicate Group Name.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (res.equals("You are unauthorized for this action")) {
            resMsg = "You are unauthorized for this action";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (res.equals("You are not an authenticated user")) {
            resMsg = "You are not an authenticated user";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            resMsg = "An error occurred.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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

        String res = adminService.newUser(requestBody);
        if (res.equals("Success")) {
            resMsg = "User Successfully Created.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (res.equals("Duplicate")) {
            resMsg = "Username Already Exists.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (res.equals("You are unauthorized for this action")) {
            resMsg = "You are unauthorized for this action.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (res.equals("You are not an authenticated user")) {
            resMsg = "You are not an authenticated user.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            resMsg = "An error occured when creating user";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUserByUsername(@Valid @RequestBody User requestBody, BindingResult bindingResult) {
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

        String res = adminService.updateUser(requestBody);
        if (res.equals("Success")) {
            resMsg = "User Successfully Updated.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (res.equals("You are unauthorized for this action")) {
            resMsg = "You are unauthorized for this action.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (res.equals("You are not an authenticated user")) {
            resMsg = "You are not an authenticated user.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            resMsg = "An error occured when updating user.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
