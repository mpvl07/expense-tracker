package com.mohan.expense_tracker.service.impl;

import com.mohan.expense_tracker.dto.login.LoginRequest;
import com.mohan.expense_tracker.dto.login.LoginResponse;
import com.mohan.expense_tracker.dto.request.CreateUserRequest;
import com.mohan.expense_tracker.dto.response.UserResponse;
import com.mohan.expense_tracker.entity.User;
import com.mohan.expense_tracker.enums.SupportedCurrency;
import com.mohan.expense_tracker.enums.UserStatus;
import com.mohan.expense_tracker.mapper.UserMapper;
import com.mohan.expense_tracker.repository.UserRepository;
import com.mohan.expense_tracker.service.JwtService;
import com.mohan.expense_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId());

        return new LoginResponse(token);

    }
    @Override
    public UserResponse getCurrentUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return userMapper.toResponse(user);
    }
    @Override
    public UserResponse register(CreateUserRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Convert DTO to Entity
        User user = userMapper.toEntity(request);

        // Business Logic
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setPreferredCurrency(SupportedCurrency.INR);

        // Save to database
        User savedUser = userRepository.save(user);

        // Convert Entity to Response DTO
        return userMapper.toResponse(savedUser);
    }
    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }
}