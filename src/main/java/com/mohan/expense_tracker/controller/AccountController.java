package com.mohan.expense_tracker.controller;

import com.mohan.expense_tracker.dto.account.AccountResponse;
import com.mohan.expense_tracker.dto.account.CreateAccountRequest;
import com.mohan.expense_tracker.entity.Account;
import com.mohan.expense_tracker.mapper.AccountMapper;
import com.mohan.expense_tracker.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestParam UUID userId,
            @Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createAccount(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountMapper.toResponse(account));
    }
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID accountId) {

        Account account = accountService.getAccountById(accountId);

        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAccounts(
            @RequestParam UUID userId,
            Pageable pageable) {

        Page<Account> accounts =
                accountService.getAccountsByUserId(userId, pageable);

        Page<AccountResponse> response =
                accounts.map(accountMapper::toResponse);

        return ResponseEntity.ok(response);
    }
}