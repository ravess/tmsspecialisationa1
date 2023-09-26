package com.tms.a1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tms.a1.entity.Application;
import com.tms.a1.entity.Plan;

import com.tms.a1.service.TmsService;

import jakarta.validation.Valid;

@RestController
public class TmsController {
    @Autowired
    private TmsService tmsService;

    // Get All Apps
    @GetMapping("/apps")
    public ResponseEntity<?> getAllApps() {
        String resMsg = "";
        Map<String, Object> response = new HashMap<>();
        List<Application> apps = tmsService.getAllApps();
        if (apps != null && !apps.isEmpty()) {
            response.put("data", apps);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            resMsg = "No Apps Found.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Get Single App
    @GetMapping("/apps/{appacronym}")
    public ResponseEntity<?> getApp(@PathVariable String appacronym) {
        String resMsg = "";
        Map<String, Object> response = new HashMap<>();
        Application app = tmsService.getApp(appacronym);
        if (app != null) {
            response.put("data", app);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            resMsg = "No Apps Found.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Create new App
    @PostMapping("/apps/new")
    public ResponseEntity<?> addNewApp(@Valid @RequestBody Application requestBody, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        String resMsg;
        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        // List permitted = adminService.checkGroup();
        // if(permitted != null && !permitted.isEmpty()){
        String res = tmsService.newApp(requestBody);

        if (res.equals("Success")) {
            resMsg = "User Successfully Created.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (res.equals("Duplicate")) {
            resMsg = "Username Already Exists.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            resMsg = "An error occured when creating user";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // }else{
        // resMsg = "You are unauthorized for this action.";
        // response.put("msg", resMsg);
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        // }
    }

    // Update one App
    @PutMapping("/apps/{appacronym}/edit")
    public ResponseEntity<?> updateAppByAppAcronym(@PathVariable String appacronym,
            @RequestBody Application requestBody,
            BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        String resMsg;

        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        // List permitted = adminService.checkGroup();
        // if(permitted != null && !permitted.isEmpty()){
        String res = tmsService.updateApp(appacronym, requestBody);
        if (res.equals("Success")) {
            resMsg = "App Successfully Updated.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (res.equals("Change some stuff here")) {
            System.out.println(res);
            resMsg = "Invalid some stuff";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (res.equals("Change some stuff here")) {
            resMsg = "Invalid some stuff";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            resMsg = "An error occurred when updating app.";
            response.put("msg", resMsg);
            System.out.println(resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // }else{
        // resMsg = "You are unauthorized for this action.";
        // response.put("msg", resMsg);
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        // }
    }

    // Get All Plans
    @GetMapping("/apps/{appacronym}/plans")
    public ResponseEntity<?> getAllPlans(@PathVariable String appacronym) {
        String resMsg = "";
        Map<String, Object> response = new HashMap<>();
        List<Plan> plans = tmsService.getAllPlans(appacronym);
        if (plans != null && !plans.isEmpty()) {
            response.put("data", plans);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            resMsg = "No Plans Found.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Get Single Plan
    @GetMapping("/apps/{appacronym}/plans/{planMVPName}")
    public ResponseEntity<Object> getPlan(@PathVariable String appacronym, @PathVariable String planMVPName) {

        String resMsg;
        Map<String, Object> response = new HashMap<>();
        Plan plan = tmsService.getPlan(planMVPName, appacronym);
        if (plan != null) {
            return new ResponseEntity<>(plan, HttpStatus.OK);
        } else {
            resMsg = "Plan does not exist";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Create new Plan
    @PostMapping("/plan/{appacronym}/new")
    public ResponseEntity<?> addNewPlan(@Valid @RequestBody Plan requestBody, @PathVariable String appacronym,
            BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        String resMsg;
        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        // List permitted = adminService.checkGroup();
        // if(permitted != null && !permitted.isEmpty()){
        String res = tmsService.newPlan(requestBody, appacronym);

        if (res.equals("Success")) {
            resMsg = "Plan Successfully Created.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (res.equals("Duplicate")) {
            resMsg = "Plan Already Exists.";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            resMsg = "An error occured when creating plan";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // }else{
        // resMsg = "You are unauthorized for this action.";
        // response.put("msg", resMsg);
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        // }
    }

    // Update Plan
    @PutMapping("/{appacronym}/editplans/{plan_mvp_name}")
    public ResponseEntity<?> updatePlanByplan_mvp_name(@PathVariable String appacronym,
            @PathVariable String plan_mvp_name,
            @RequestBody Plan requestBody,
            BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        String resMsg;

        if (bindingResult.hasErrors()) {
            // Handle validation errors here
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put("msg", fieldError.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        // List permitted = adminService.checkGroup();
        // if(permitted != null && !permitted.isEmpty()){
        String res = tmsService.updatePlan(appacronym, plan_mvp_name, requestBody);
        if (res.equals("Success")) {
            resMsg = "Plan Successfully Updated.";
            response.put("msg", resMsg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (res.equals("Change some stuff here")) {
            System.out.println(res);
            resMsg = "Invalid some stuff";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (res.equals("Change some stuff here")) {
            resMsg = "Invalid some stuff";
            response.put("msg", resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            resMsg = "An error occurred when updating plan.";
            response.put("msg", resMsg);
            System.out.println(resMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // }else{
        // resMsg = "You are unauthorized for this action.";
        // response.put("msg", resMsg);
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        // }
    }

   

    


    }
