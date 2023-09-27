package com.tms.a1.config.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tms.a1.dao.UserDAO;
import com.tms.a1.entity.Application;
import com.tms.a1.entity.User;
import com.tms.a1.exception.ForbiddenException;
import com.tms.a1.service.AuthService;
import com.tms.a1.service.TmsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public class AppPermitFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private TmsService tmsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the current URL
        if (request.getRequestURI().startsWith("/apps/")) {
            // Extract the {appacronym} part from the URL
            String[] parts = request.getRequestURI().split("/");
            if (parts.length >= 2) {
                String appacronym = parts[2];
                Application app = tmsService.getApp(appacronym);

                // Create a map to store app permit details
                Map<String, Object> appPermitDetails = new HashMap<>();
                // Get the Authentication object from the SecurityContextHolder
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // Get the authorities (roles) associated with the authenticated user
                List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
                // Iterate through the authorities to check against your database records

                for (GrantedAuthority authority : authorities) {
                    String role = authority.getAuthority();
                    // Call the checkGroup function to check if the user has this role in your database
                    boolean hasPermission = authService.checkgroup2(authentication.getName(), role);

                }

                // Set the map of app permit details as a detail in the Authentication object
                ((UsernamePasswordAuthenticationToken) authentication).setDetails(appPermitDetails);
            }
        }
        filterChain.doFilter(request, response);
    }
}
