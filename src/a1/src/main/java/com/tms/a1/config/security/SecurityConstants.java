package com.tms.a1.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties") // Use this annotation to specify the properties file
public class SecurityConstants {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${token.expiration}")
    private int tokenExpiration;

    @Value("${cookie.name}")
    private String cookieName;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public int getTokenExpiration() {
        return tokenExpiration;
    }

    public String getCookieName() {
        return cookieName;
    }
}

