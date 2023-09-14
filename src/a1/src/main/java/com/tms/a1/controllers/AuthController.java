package com.tms.a1.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tms.a1.entity.User;
import com.tms.a1.repository.UserRepo;
import com.tms.a1.service.AuthService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {


  private UserRepo userRepo;
  private BCryptPasswordEncoder passwordEncoder;

  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Map<String, String> requestBody) {
    String username = requestBody.get("username");
    String plainTextPassword = requestBody.get("password");

    // Retrieve the user from the database based on the provided username
    Optional<User> optionalUser = userRepo.findByUsername(username);

    if (!optionalUser.isPresent()) {
      // User not found
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User user = optionalUser.get();

    // Verify the password using BCrypt
    if (passwordEncoder.matches(plainTextPassword, user.getPassword())) {
      // Passwords match, login successful
      return ResponseEntity.status(HttpStatus.OK).body("Login successful");
    } else {
      // Passwords do not match
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
    }
  }
  @PostMapping("/checkgroup")
  public ResponseEntity<Map<String,Object>> CheckGroup (@RequestBody Map<String, String> requestBody ) {
      return new ResponseEntity<>(authService.checkgroup(requestBody), HttpStatus.OK);
    }
    }

