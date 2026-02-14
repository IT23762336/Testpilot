package com.testlang.demo.service;

import com.testlang.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private Map<Integer, User> users = new HashMap<>();
    
    public UserService() {
        // Initialize with sample data
        users.put(42, new User(42, "alice", "Alice Johnson", "alice@example.com", "USER"));
        users.put(100, new User(100, "bob", "Bob Smith", "bob@example.com", "ADMIN"));
    }
    
    public User getUserById(Integer id) {
        return users.get(id);
    }
}