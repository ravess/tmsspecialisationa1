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

import jakarta.persistence.EntityNotFoundException;

@RestController
public class AuthController {
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private GroupRepo groupRepo;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/checkgroup")
  public ResponseEntity<String> CheckGroup (@RequestBody Map<String, String> requestBody ) {
    String username = requestBody.get("username"); //Should be from JWToken instead of req body
    String group = requestBody.get("group");
    String result = userRepo.checkgroup(username,group);
    if (result != null){
      String resMsg = "True";
      return new ResponseEntity<>(resMsg, HttpStatus.OK);
    }else{
      String resMsg = "False";
      return new ResponseEntity<>(resMsg, HttpStatus.OK);
    }
    }
}
