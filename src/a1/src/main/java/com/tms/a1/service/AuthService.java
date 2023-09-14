package com.tms.a1.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.tms.a1.dao.UserDAO;
import com.tms.a1.entity.User;
import com.tms.a1.repository.UserRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {
 
  private UserDAO userRepo;
 

    public Map<String, Object> checkgroup(Map<String, String> requestBody) {
        String username = requestBody.get("username"); // Should be from JWToken instead of req body
        String group = requestBody.get("group");
        String result = userRepo.checkgroup(username, group);
        if (result != null) {
            String resMsg = "True";
            Map<String, Object> response = new HashMap<>();
            response.put("ingroup", resMsg);
            return response;
        } else {
            String resMsg = "False";
            Map<String, Object> response = new HashMap<>();
            response.put("ingroup", resMsg);
            return response;
        }
    }
 
}
