package com.tms.a1.config.security;


import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.tms.a1.config.security.filter.AuthenticationFilter;
import com.tms.a1.config.security.filter.ExceptionHandlerFilter;
import com.tms.a1.config.security.filter.JWTAuthorizationFilter;
import com.tms.a1.config.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private CustomAuthenticationManager customAuthenticationManager;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
    authenticationFilter.setFilterProcessesUrl("/login2");
    
    http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(authorize -> authorize
        //.requestMatchers("/**").permitAll()  
        .anyRequest().authenticated())
  
    .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
    .addFilter(authenticationFilter)
    .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

}
