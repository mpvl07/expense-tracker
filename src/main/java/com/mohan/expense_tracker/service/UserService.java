package com.mohan.expense_tracker.service;

import com.mohan.expense_tracker.dto.login.LoginRequest;
import com.mohan.expense_tracker.dto.login.LoginResponse;
import com.mohan.expense_tracker.dto.request.CreateUserRequest;
import com.mohan.expense_tracker.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    LoginResponse login(LoginRequest request);
    UserResponse register(CreateUserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getCurrentUser(UUID userId);
}