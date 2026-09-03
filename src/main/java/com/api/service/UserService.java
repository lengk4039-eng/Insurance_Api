package com.api.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.dto.CreateUserRequest;
import com.api.entity.User;
import com.api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Creates a new admin/staff/consultant login: hashes the raw password
     * with BCrypt before saving, so it can later be checked by
     * AuthController's login endpoint.
     */
    public User registerUser(CreateUserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setIsActive(true);
        return userRepository.save(user);
    }
}
