package com.mohan.expense_tracker.service;

import com.mohan.expense_tracker.dto.account.CreateAccountRequest;
import com.mohan.expense_tracker.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AccountService {

    Account createAccount(UUID userId, CreateAccountRequest request);

    Account getAccountById(UUID accountId);

    Page<Account> getAccountsByUserId(UUID userId, Pageable pageable);
}