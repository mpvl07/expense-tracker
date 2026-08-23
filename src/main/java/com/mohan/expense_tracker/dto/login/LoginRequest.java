package com.mohan.expense_tracker.dto.login;

public record LoginRequest(
        String email,
        String password
) {
}