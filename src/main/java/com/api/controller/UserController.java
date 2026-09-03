package com.api.controller;

import com.api.dto.CreateUserRequest;
import com.api.entity.User;
import com.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Creates a new login (ADMIN / CONSULTANT / STAFF). Use this to create
     * your first test admin user, e.g. with Thunder Client:
     * POST http://localhost:8081/api/users
     * { "fullName": "Admin One", "username": "admin1", "password": "Passw0rd!",
     *   "email": "admin1@company.com", "role": "ADMIN" }
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User created = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
