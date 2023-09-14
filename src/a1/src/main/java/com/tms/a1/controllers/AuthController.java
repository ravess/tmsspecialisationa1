package com.tms.a1.controllers;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tms.a1.config.security.SecurityConstants;

import com.tms.a1.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {

  private AuthService authService;



  @PostMapping("/logout2")
  public ResponseEntity<Object> logout(HttpServletResponse response) {
    // Remove the JWT token cookie by creating a new cookie with an expired date
    Cookie cookie = new Cookie(SecurityConstants.COOKIE_NAME, "");
    cookie.setPath("/");
    cookie.setMaxAge(0); // Set the max age to 0 to expire the cookie immediately
    response.addCookie(cookie);
    String message = "Logged out successfully";
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

<<<<<<< HEAD
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
=======
  @PostMapping("/checkgroup")
  public ResponseEntity<Map<String, Object>> CheckGroup(@RequestBody Map<String, String> requestBody) {
    return new ResponseEntity<>(authService.checkgroup(requestBody), HttpStatus.OK);
  }
>>>>>>> d1758617c3ca36db5d4141fdc739b08e7286f7d5
}
