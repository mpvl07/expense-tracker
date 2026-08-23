package com.mohan.expense_tracker.service.impl;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.entity.Account;
import com.mohan.expense_tracker.entity.Transaction;
import com.mohan.expense_tracker.enums.TransactionType;
import com.mohan.expense_tracker.exception.InsufficientBalanceException;
import com.mohan.expense_tracker.exception.ResourceNotFoundException;
import com.mohan.expense_tracker.repository.AccountRepository;
import com.mohan.expense_tracker.repository.TransactionRepository;
import com.mohan.expense_tracker.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Transaction createTransaction(
            UUID accountId,
            UUID userId,
            CreateTransactionRequest request,
            String idempotencyKey) {

        Optional<Transaction> existing =
                transactionRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            return existing.get();
        }

        Account account = accountRepository
                .findByIdAndUserId(accountId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountId));

        BigDecimal currentBalance = account.getBalance();

        BigDecimal newBalance;

        if (request.getType() == TransactionType.CREDIT) {
            newBalance = currentBalance.add(request.getAmount());
        } else {
            newBalance = currentBalance.subtract(request.getAmount());
        }

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient account balance");
        }

        account.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setIdempotencyKey(idempotencyKey);

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransaction(
            UUID accountId,
            UUID transactionId,
            UUID userId) {

        // First verify account ownership
        accountRepository
                .findByIdAndUserId(accountId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountId));

        return transactionRepository
                .findByIdAndAccountId(transactionId, accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found: " + transactionId));
    }
    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactions(
            UUID accountId,
            UUID userId,
            Pageable pageable) {

        // Verify account ownership
        accountRepository
                .findByIdAndUserId(accountId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountId));

        return transactionRepository
                .findByAccountId(accountId, pageable);
    }
}