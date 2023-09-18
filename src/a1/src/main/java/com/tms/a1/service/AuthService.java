package com.tms.a1.service;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tms.a1.dao.UserDAO;

import lombok.AllArgsConstructor;


@Service
public class AuthService {
 
  private UserDAO userRepo;

    public Map<String, Object> checkgroup(Map<String, String> requestBody) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName(); // Should be from JWToken instead of req body
        String group = requestBody.get("group");
        List result = userRepo.checkgroup(username, group);
        if (result != null && !result.isEmpty()) {
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
    }return null;
}catch (Exception e) {
    System.out.println(e);
    return null;
}
    }
}
