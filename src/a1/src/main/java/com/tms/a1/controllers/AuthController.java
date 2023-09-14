package com.tms.a1.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tms.a1.config.security.SecurityConstants;
import com.tms.a1.entity.User;
import com.tms.a1.repository.UserRepo;
import com.tms.a1.service.AuthService;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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

  @PostMapping("/logout2")
  public ResponseEntity<Object> logout(HttpServletResponse response) {
    System.out.println("It came in logout request");
    // Remove the JWT token cookie by creating a new cookie with an expired date
    Cookie cookie = new Cookie(SecurityConstants.COOKIE_NAME, "");
    cookie.setPath("/");
    cookie.setMaxAge(0); // Set the max age to 0 to expire the cookie immediately
    response.addCookie(cookie);
    String message = "Logged out successfully";
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @PostMapping("/checkGroup")
  public ResponseEntity<Map<String,Object>> CheckGroup (@RequestBody Map<String, String> requestBody ) {
    return new ResponseEntity<>(authService.checkgroup(requestBody), HttpStatus.OK);
  }

  @GetMapping("/validateCookie")
  public ResponseEntity<?> checkCookie(){
    Boolean resMsg = true;
    Map<String, Object> response = new HashMap<>();
    response.put("hasCookie", resMsg);
    // The user is not in the group, return unauthorized
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
