package com.tms.a1.service;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tms.a1.repository.UserRepo;

@AllArgsConstructor
@Service
public class AuthService {
    @Autowired
  private UserRepo userRepo;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  public Map<String,Object> checkgroup(Map<String,String> requestBody){
    String username = requestBody.get("username"); //Should be from JWToken instead of req body
    String group = requestBody.get("group");
    String result = userRepo.checkgroup(username,group);
    if (result != null){
      String resMsg = "True";
      Map<String, Object> response = new HashMap<>();
    response.put("ingroup", resMsg);
      return response;
    }else{
      String resMsg = "False";
      Map<String, Object> response = new HashMap<>();
      response.put("ingroup", resMsg);
      return response;
    }
  }
}
