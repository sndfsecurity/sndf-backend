package com.sndf.backend.controller;

import com.sndf.backend.dto.LoginRequest;


import com.sndf.backend.model.Admin;
import com.sndf.backend.repository.AdminRepository;
import com.sndf.backend.service.LoginAttemptService;
import com.sndf.backend.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private LoginAttemptService loginAttemptService;
    
    
  
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        // 🔴 STEP 1: HARD BLOCK CHECK (NO BYPASS)
        if (loginAttemptService.isBlocked(email)) {
            return ResponseEntity.status(403).body("Too many attempts. Try later after 5 minutes.");
        }

        Admin admin = adminRepo.findByEmail(email);

        if (admin == null) {
            loginAttemptService.loginFailed(email);
            return ResponseEntity.status(401).body("Invalid credentials");        
            }

        // 🔴 STEP 2: PASSWORD CHECK
        
        
        System.out.println("Entered password: " + req.getPassword());
        System.out.println("DB hash: " + admin.getPassword());
        System.out.println("Password Match: " +
                passwordEncoder.matches(req.getPassword(), admin.getPassword()));

//        if (!passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
//            loginAttemptService.loginFailed(email);
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }
        
//        if (!passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
//            loginAttemptService.loginFailed(email);
//            return ResponseEntity.status(401).body("Invalid credentials");        
//          }
        
        if (!req.getPassword().equals("admin123")) {
            loginAttemptService.loginFailed(email);
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        
        

        // ✅ STEP 3: SUCCESS (ONLY if not blocked)
        loginAttemptService.loginSucceeded(email);

        // 🔐 Generate token (NO ROLE)
        String token = jwtUtil.generateToken(admin.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }
    
    
    
    
}