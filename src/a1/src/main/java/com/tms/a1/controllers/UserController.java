package com.tms.a1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.tms.a1.service.UserService;

public class UserController {

    @Autowired
    private UserService userService;
    
    //user get own profile
    // @GetMapping("/getUser")
    // public ResponseEntity<> getOwnProfile(){
    //     String username = userService.getUserFromToken();

    // }
}
