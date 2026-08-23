package com.mohan.expense_tracker.service.impl;

import com.mohan.expense_tracker.dto.account.CreateAccountRequest;
import com.mohan.expense_tracker.entity.Account;
import com.mohan.expense_tracker.entity.User;
import com.mohan.expense_tracker.enums.SupportedCurrency;
import com.mohan.expense_tracker.exception.ResourceNotFoundException;
import com.mohan.expense_tracker.mapper.AccountMapper;
import com.mohan.expense_tracker.repository.AccountRepository;
import com.mohan.expense_tracker.repository.UserRepository;
import com.mohan.expense_tracker.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public Account createAccount(UUID userId, CreateAccountRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        SupportedCurrency currency = request.getCurrency() != null
                ? request.getCurrency()
                : user.getPreferredCurrency();

        Account account = new Account();

        account.setUser(user);
        account.setName(request.getName());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);

        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)

    public Account getAccountById(UUID accountId, UUID userId) {

        return accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
    @Override
    @Transactional(readOnly = true)
    public Page<Account> getAccountsByUserId(
            UUID userId,
            Pageable pageable) {

        return accountRepository.findByUserId(userId, pageable);
    }

}