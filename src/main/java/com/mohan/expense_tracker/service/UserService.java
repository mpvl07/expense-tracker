package com.mohan.expense_tracker.service;

import com.mohan.expense_tracker.dto.request.CreateUserRequest;
import com.mohan.expense_tracker.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse register(CreateUserRequest request);
    List<UserResponse> getAllUsers();

}