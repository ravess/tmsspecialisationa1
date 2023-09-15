package com.tms.a1.config.security.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tms.a1.config.security.SecurityConstants;
import com.tms.a1.exception.ForbiddenException;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityConstants securityConstants;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the JWT token from the cookie
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(securityConstants.getCookieName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null ) {
            throw new ForbiddenException("You are not logged in!");
            // filterChain.doFilter(request, response);
            // return;
        }

    
        DecodedJWT  decodedJWT = JWT.require(Algorithm.HMAC512(securityConstants.getJwtSecret()))
            .build()
            .verify(token);
        
        String user = decodedJWT.getSubject();
        String ipAddress = decodedJWT.getClaim("ipAddress").asString();
        String browser = decodedJWT.getClaim("userAgent").asString();
        
        String clientIpAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        if(!ipAddress.equals(clientIpAddress) || !userAgent.equals(browser)){
            throw new ForbiddenException("Please Log in again");
        }

        System.out.println("WHOAMI");
        System.out.println("USER: " + user);

        //"Authentication token"
        //UsernamePWAuthToken = "app-wide authentication token"
        //1st param: authenticated user's username, 2nd param: user's credentials, set as null as pw not needed after authentication, 3rd param list of roles associated with user. use Arrays.asList() for none. 
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList());

        //the following stores details of currently authenticated user, providing a way to access the authenticated info throughout the app
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
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
