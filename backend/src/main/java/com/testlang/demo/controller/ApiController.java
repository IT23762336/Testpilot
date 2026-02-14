package com.testlang.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlang.demo.model.LoginRequest;
import com.testlang.demo.model.LoginResponse;
import com.testlang.demo.model.UpdateRequest;
import com.testlang.demo.model.User;
import com.testlang.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private UserService userService;
    
    /**
     * POST /api/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Accept multiple valid credentials
        boolean validCredentials = 
            ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) ||
            ("testuser".equals(request.getUsername()) && "test123".equals(request.getPassword()));
        
        if (validCredentials) {
            LoginResponse response = new LoginResponse();
            response.setToken("fake-jwt-token-12345");
            response.setUsername(request.getUsername());
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
        }
        return ResponseEntity.status(401).build();
    }
    
    /**
     * GET /api/users/{id}
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * PUT /api/users/{id}
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateRequest request) {
        
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Update user
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("updated", true);
        response.put("id", id);
        response.put("role", user.getRole());
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .header("X-App", "TestLangDemo")
            .body(response);
    }
    
    /**
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        response.put("deleted", true);
        response.put("id", id);
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(response);
    }
}