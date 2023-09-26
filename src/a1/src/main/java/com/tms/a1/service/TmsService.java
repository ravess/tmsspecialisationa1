package com.tms.a1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tms.a1.dao.TmsDAO;
import com.tms.a1.dao.UserDAO;
import com.tms.a1.entity.Application;
import com.tms.a1.entity.Plan;
import com.tms.a1.entity.Task;
import com.tms.a1.exception.EntityNotFoundException;


@Service
public class TmsService {
    @Autowired
    private TmsDAO tmsRepo;

    @Autowired
  private UserDAO userRepo;

    //Get All Apps
    public List<Application> getAllApps() {
        return tmsRepo.findAllApps();
    }

    //Get Single App
    public Application getApp(String appacronym) {
        try {
            Application app = tmsRepo.findByApp(appacronym);
            if (app != null) {
                return app;
            } else {
                throw new EntityNotFoundException(appacronym, Application.class);
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //Create New App
    public String newApp(Application app) {
        if (tmsRepo.existByAppAcronym(app.getAppAcronym())) {
            return "Duplicate";
        }
        String appDescription = app.getAppDescription();
        String appPermitCreate = app.getAppPermitCreate();
        String appPermitOpen = app.getAppPermitOpen();
        String appPermitToDoList = app.getAppPermitToDoList();
        String appPermitDoing = app.getAppPermitDoing();
        String appPermitDone = app.getAppPermitDone();
        String appStartDate = app.getAppStartDate();
        String appEndDate = app.getAppEndDate();

        app.setAppDescription("To be amended accrodingly");
        app.setAppPermitCreate("To be amended accrodingly");
        app.setAppPermitOpen("To be amended accrodingly");
        app.setAppStartDate("To be amended accrodingly");
        tmsRepo.saveApp(app);
        return "Success";
    }

    //Update App
    public String updateApp(String appacronym, Application app) {
        Application existingApp = tmsRepo.findByApp(appacronym);
        if (existingApp != null) {
            // Update the app's information
            // String plainTextPassword = user.getPassword();
            // String email = user.getEmail();
            // String groupToUpdate = user.getGroups();
            // int isActive = user.getIsActive();

            // Hash the new password using BCrypt if provided and not empty
            // if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
            //     if (!isPasswordValid(plainTextPassword)) {
            //         return "Invalid password";
            //     }
            //     String hashedPassword = passwordEncoder.encode(plainTextPassword);
            //     existingUser.setPassword(hashedPassword);
            // }

            // if (email != null && !email.isEmpty()) {
            //     if (!isValidEmail(email)) {
            //         return "Invalid email";
            //     }
            //     existingUser.setEmail(email);
            // }

            // existingUser.setGroups(groupToUpdate);
            // existingUser.setIsActive(isActive);

            // Save the updated user back to the repository
            tmsRepo.saveApp(existingApp);

            return "Success";
        } else {
            return "App not found";
        }
    }

    //Get All Plans
    public List<Plan> getAllPlans(String appacronym) {
        return tmsRepo.findAllPlans(appacronym);
    }

    //Get single Plan
    public Plan getPlan(String planid, String appacronym) {
        try {
            Plan plan = tmsRepo.findByPlan(planid, appacronym);
            if (plan != null) {
                return plan;
            } else {
                throw new EntityNotFoundException(planid, Plan.class);
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //Create New Plan
    public String newPlan(Plan plan, String appacronym) {
        //Do something here, you have a appacronym  below is just boilerplate for you to amend accordingly
        //check if app exists
        if(!tmsRepo.existByAppAcronym(appacronym)){
            return "NonexistentApp";
        }
        
        //check for duplicate plan name
        if (tmsRepo.existByPlanMVPName(plan.getPlanMVPName())) {
            return "Duplicate";
        }
        
        tmsRepo.savePlan(plan);
        return "Success";
    }


    //Update plan
    public String updatePlan(String appacronym, String planid, Plan plan) {
        Plan existingPlan = tmsRepo.findByPlan(planid, appacronym);
        if (existingPlan != null) {
            // Update the user's information
            // String plainTextPassword = user.getPassword();
            // String email = user.getEmail();
            // String groupToUpdate = user.getGroups();
            // int isActive = user.getIsActive();

            // Hash the new password using BCrypt if provided and not empty
            // if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
            //     if (!isPasswordValid(plainTextPassword)) {
            //         return "Invalid password";
            //     }
            //     String hashedPassword = passwordEncoder.encode(plainTextPassword);
            //     existingUser.setPassword(hashedPassword);
            // }

            // if (email != null && !email.isEmpty()) {
            //     if (!isValidEmail(email)) {
            //         return "Invalid email";
            //     }
            //     existingUser.setEmail(email);
            // }

            // existingUser.setGroups(groupToUpdate);
            // existingUser.setIsActive(isActive);

            // Save the updated user back to the repository
            // userRepo.saveUser(existingUser);

            return "Success";
        } else {
            return "User not found";
        }
    }

    //Get All Tasks
    public List<Task> getAllTasks(String appacronym) {
        return tmsRepo.findAllTasks(appacronym);
    }

    //Get Single Task
    public Task getTask(String taskid, String appacronym) {
        try {
            Task task = tmsRepo.findByTask(taskid, appacronym);
            if (task != null) {
                return task;
            } else {
                throw new EntityNotFoundException(taskid, Task.class);
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //Create New Task
    public String newTask(Task task, String appacronym) {
        //Do something here, you have a appacronym  below is just boilerplate for you to amend accordingly
        if (tmsRepo.existByTaskID(task.getTaskID())) {
            return "Duplicate";
        }
        String taskDescription = task.getTaskDescription();
        String taskNotes = task.getTaskNotes();
        String taskStates = task.getTaskState();

        task.setTaskDescription("To be amended accrodingly");
        task.setTaskNotes("To be amended accrodingly");
        task.setTaskState("To be amended accrodingly");
        tmsRepo.saveTask(task);
        return "Success";
    }

    //Update Task
    public String updateTask(String appacronym, String taskid, Task task) {
        Task existingTask = tmsRepo.findByTask(taskid, appacronym);
        if (existingTask != null) {
            // Update the user's information
            // String plainTextPassword = user.getPassword();
            // String email = user.getEmail();
            // String groupToUpdate = user.getGroups();
            // int isActive = user.getIsActive();

            // Hash the new password using BCrypt if provided and not empty
            // if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
            //     if (!isPasswordValid(plainTextPassword)) {
            //         return "Invalid password";
            //     }
            //     String hashedPassword = passwordEncoder.encode(plainTextPassword);
            //     existingUser.setPassword(hashedPassword);
            // }

            // if (email != null && !email.isEmpty()) {
            //     if (!isValidEmail(email)) {
            //         return "Invalid email";
            //     }
            //     existingUser.setEmail(email);
            // }

            // existingUser.setGroups(groupToUpdate);
            // existingUser.setIsActive(isActive);

            // Save the updated user back to the repository
            // userRepo.saveUser(existingUser);

            return "Success";
        } else {
            return "User not found";
        }
    }

    //Has Access to
    public Boolean hasAccess(Map<String, String> requestBody) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName(); 
        String app = requestBody.get("app_acronym");
        String state = requestBody.get("app_state");
        List<String> grouplist = tmsRepo.getPermit(app,state);
        String group  = grouplist.get(0);
        List result = userRepo.checkgroup(username, group);
        if (result != null && !result.isEmpty()) {
            
            return true;
        } else {
            
            return false;
        }
    }return null;
}catch (Exception e) {
    System.out.println(e);
    return null;
}
    }

}
