package com.tms.a1.config.security.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.tms.a1.entity.User;
import com.tms.a1.exception.EntityNotFoundException;
import com.tms.a1.repository.UserRepo;



@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> optionalUser = userRepo.findByUsername(authentication.getName());
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException(authentication.getName(), User.class);
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Username/Password");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }



}
