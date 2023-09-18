package com.tms.a1.config.security.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tms.a1.config.security.SecurityConstants;
import com.tms.a1.config.security.manager.CustomAuthenticationManager;
import com.tms.a1.entity.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private SecurityConstants securityConstants;

    private CustomAuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        } 
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        
        // Get the client's IP address from the request
        String clientIpAddress = getClientIpAddress(request);
        
        // Get the user agent (browser type) from the request
        String userAgent = request.getHeader("User-Agent");

        String token = JWT.create()
            .withSubject(authResult.getName())  //username is saved as 'subject'
            .withExpiresAt(new Date(System.currentTimeMillis() + securityConstants.getTokenExp()))
            .withClaim("ipAddress", clientIpAddress) // Add IP address as a custom claim
            .withClaim("userAgent", userAgent) // Add user agent as a custom claim
            .sign(Algorithm.HMAC512(securityConstants.getSecretKey()));
        // Create a cookie to hold the JWT token
        Cookie cookie = new Cookie(securityConstants.getCookieName(), token);
        
        // Set the cookie's path and other attributes as needed
        cookie.setPath("/"); // Set the cookie path to "/" to make it accessible to all paths
        cookie.setMaxAge(securityConstants.getTokenExp() / 1000); // Set the cookie's max age in seconds
        cookie.setHttpOnly(true); // Make the cookie HTTP-only for added security
        
        // Add the cookie to the response
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("You are logged In!");
        response.getWriter().flush();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
