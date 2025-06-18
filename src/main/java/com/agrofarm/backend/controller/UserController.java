package com.agrofarm.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agrofarm.backend.dto.AuthRequest;
import com.agrofarm.backend.dto.RegisterRequest;
import com.agrofarm.backend.entity.User;
import com.agrofarm.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

        @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Map.of("message", "Registered user: " + user.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            User user = userService.loginUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("Успешный вход: " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}

