package com.api.dto;

/**
 * Response body for POST /api/auth/login.
 * Field names must match the Angular app's LoginResponse interface exactly
 * (src/app/core/models/auth.model.ts in the Web-admin repo).
 */
public class LoginResponse {

    private String token;
    private Integer userId;
    private String username;
    private String email;
    private String role;

    public LoginResponse(String token, Integer userId, String username, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
