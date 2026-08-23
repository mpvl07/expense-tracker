package com.mohan.expense_tracker.controller;

import com.mohan.expense_tracker.dto.login.LoginRequest;
import com.mohan.expense_tracker.dto.login.LoginResponse;
import com.mohan.expense_tracker.dto.request.CreateUserRequest;
import com.mohan.expense_tracker.dto.response.UserResponse;
import com.mohan.expense_tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(userService.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.ok(userService.register(request));
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UUID userId) {

        return ResponseEntity.ok(
                userService.getCurrentUser(userId)
        );
    }
}