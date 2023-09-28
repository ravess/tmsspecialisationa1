package com.tms.a1.config.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tms.a1.entity.Application;
import com.tms.a1.entity.Task;
import com.tms.a1.exception.ForbiddenException;
import com.tms.a1.service.AuthService;
import com.tms.a1.service.TmsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


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
            String appacronym = parts[2];
            Application app = tmsService.getApp(appacronym);

            // Get the Authentication object from the SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Get the authorities (roles) associated with the authenticated user
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

            if (parts.length == 4) {
                String permitCreate = app.getAppPermitCreate();
                GrantedAuthority therole = new SimpleGrantedAuthority(permitCreate);
                    
                if(!authorities.contains(therole)){
                    throw new ForbiddenException("unauthorized");
                }
            } else if (parts.length == 5) {
                String permitOpen = app.getAppPermitOpen();
                String permitTodo = app.getAppPermitToDoList();
                String permitDoing = app.getAppPermitDoing();
                String permitDone = app.getAppPermitDone();

                String taskid = parts[4];
                Task task = tmsService.getTask(taskid, appacronym);
                String taskCurrentState = task.getTaskState(); 
                System.out.println("TASK CURRENT STATE: "+taskCurrentState);

                System.out.println("user's roles: "+ authorities);

                if(taskCurrentState.equals("OPEN")){
                    GrantedAuthority therole = new SimpleGrantedAuthority(permitOpen);
                    
                    if(!authorities.contains(therole)){
                        throw new ForbiddenException("unauthorized");
                    }
                } else if (taskCurrentState.equals("TODO")){
                    GrantedAuthority therole = new SimpleGrantedAuthority(permitTodo);
                    
                    if(!authorities.contains(therole)){
                        throw new ForbiddenException("unauthorized");
                    }
                } else if (taskCurrentState.equals("DOING")){
                    GrantedAuthority therole = new SimpleGrantedAuthority(permitDoing);
                    
                    if(!authorities.contains(therole)){
                        throw new ForbiddenException("unauthorized");
                    }
                } else if (taskCurrentState.equals("DONE")){
                    GrantedAuthority therole = new SimpleGrantedAuthority(permitDone);
                    
                    if(!authorities.contains(therole)){
                        throw new ForbiddenException("unauthorized");
                    }
                }


                // // Create a map to store app permit details
                // Map<String, Object> appPermitDetails = new HashMap<>();

                // // Iterate through the authorities to check against your database records
                // for (GrantedAuthority authority : authorities) {
                //     String role = authority.getAuthority();
                //     // Call the checkGroup function to check if the user has this role in your database
                //     boolean hasPermission = authService.checkgroup2(authentication.getName(), role);
                // }

                // // Set the map of app permit details as a detail in the Authentication object
                // ((UsernamePasswordAuthenticationToken) authentication).setDetails(appPermitDetails);
            }
        }
        filterChain.doFilter(request, response);
    }
}
