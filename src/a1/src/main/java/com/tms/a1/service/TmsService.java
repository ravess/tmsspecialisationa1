package com.tms.a1.service;

import java.lang.reflect.Field;
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
import com.tms.a1.entity.User;
import com.tms.a1.exception.EntityNotFoundException;

@Service
public class TmsService {
    @Autowired
    private TmsDAO tmsRepo;

    @Autowired
    private UserDAO userRepo;

    // Get All Apps
    public List<Application> getAllApps() {
        return tmsRepo.findAllApps();
    }

    // Get Single App
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

    // Get Single App
    public Application getAppPermit(String appacronym) {
        try {
            Application app = tmsRepo.findAppPermitsByApp(appacronym);
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

    // Create New App
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

       
        tmsRepo.saveApp(app);
        return "Success";
    }

    // Update App
    public String updateApp(String appacronym, Application app) {
        Application existingApp = tmsRepo.findByApp(appacronym);
        if (existingApp != null) {
            existingApp.setAppStartDate(app.getAppStartDate());
            existingApp.setAppEndDate(app.getAppEndDate());
            existingApp.setAppPermitOpen(app.getAppPermitOpen());
            existingApp.setAppPermitToDoList(app.getAppPermitToDoList());
            existingApp.setAppPermitDoing(app.getAppPermitDoing());
            existingApp.setAppPermitDone(app.getAppPermitDone());
            existingApp.setAppPermitCreate(app.getAppPermitCreate());
            
            tmsRepo.saveApp(existingApp);
            return "Success";
        } else {
            return "App not found";
        }
    }

    // Get All Plans
    public List<Plan> getAllPlans(String appacronym) {
        return tmsRepo.findAllPlans(appacronym);
    }

    // Get single Plan
    public Plan getPlan(String planid, String appacronym) {
        try {
            System.out.println("********************");
            System.out.println("In plan service layer");
            System.out.println(planid);
            System.out.println("*********************");
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

    // Create New Plan
    public String newPlan(Plan plan, String appacronym) {
        // check if app exists
        if (!tmsRepo.existByAppAcronym(appacronym)) {
            return "NonexistentApp";
        }

        // check for duplicate plan name
        if (tmsRepo.existByPlanMVPName(plan.getPlanMVPName())) {
            return "Duplicate";
        }
        plan.setPlanAppAcronym(appacronym);
        tmsRepo.savePlan(plan);
        return "Success";
    }

    // Update plan
    public String updatePlan(String appacronym, String planid, Plan plan) {
        Plan existingPlan = tmsRepo.findByPlan(planid, appacronym);
        System.out.println(existingPlan);
        if (existingPlan != null) {
            // Update plans's information
            // Iterate over the fields of the Plan class
            for (Field field : Plan.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    // Get the field name and value from the request body
                    String fieldName = field.getName();
                    Object fieldValue = field.get(plan);

                    // Check if the field value is not null and update the existingPlan
                    if (fieldValue != null) {
                        Field existingField = Plan.class.getDeclaredField(fieldName);
                        existingField.setAccessible(true);
                        existingField.set(existingPlan, fieldValue);
                    }

                } catch (Exception e) {
                    // Handle any exceptions or errors
                    e.printStackTrace();
                    return "Error updating plan";
                }
            }
            // Save the updated plan back to the repository
            tmsRepo.savePlan(existingPlan);
            return "Success";
        } else {
            return "Plan not found";
        }
    }

    // Get All Tasks
    public List<Task> getAllTasks(String appacronym) {
        return tmsRepo.findAllTasks(appacronym);
    }

    // Get Single Task
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

    // Create New Task
    public String newTask(Task task, String appacronym) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                task.setTaskCreator(username);
                task.setTaskOwner(username);
                task.setTaskState("Open");
                task.setTaskAppAcronym(appacronym);
                Application application = tmsRepo.findByApp(appacronym);
                int appRNumber = application.getAppRNumber();
                task.setTaskID(appacronym + "_" + appRNumber);

                if (task.getTaskName() == null || task.getTaskName().isEmpty()){
                    return "Please input task name";
                }
                if (task.getTaskDescription() == null || task.getTaskDescription().isEmpty()){
                    return "Please input task description";
                }

                tmsRepo.saveTask(task);
                application.setAppRNumber(appRNumber + 1);
                tmsRepo.saveApp(application);

                System.out.println(application);
                System.out.println(task);
                return "Success";
            } else {
                throw new EntityNotFoundException("You are not an authenticated user", User.class);
            }
        } catch (

        Exception e) {
            e.printStackTrace(); // Log the exception stack trace
            return "An error occurred: " + e.getMessage(); // Return the exception message
        }
    }

    // Update Task
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
            // if (!isPasswordValid(plainTextPassword)) {
            // return "Invalid password";
            // }
            // String hashedPassword = passwordEncoder.encode(plainTextPassword);
            // existingUser.setPassword(hashedPassword);
            // }

            // if (email != null && !email.isEmpty()) {
            // if (!isValidEmail(email)) {
            // return "Invalid email";
            // }
            // existingUser.setEmail(email);
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

    // Has Access to
    public Boolean hasAccess(Map<String, String> requestBody) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                String app = requestBody.get("app_acronym");
                String state = requestBody.get("app_state");
                List<String> grouplist = tmsRepo.getPermit(app, state);
                String group = grouplist.get(0);
                List result = userRepo.checkgroup(username, group);
                if (result != null && !result.isEmpty()) {

                    return true;
                } else {

                    return false;
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
