package com.mohan.expense_tracker.service.impl;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.entity.Account;
import com.mohan.expense_tracker.entity.Transaction;
import com.mohan.expense_tracker.enums.TransactionType;
import com.mohan.expense_tracker.repository.AccountRepository;
import com.mohan.expense_tracker.repository.TransactionRepository;
import com.mohan.expense_tracker.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
            CreateTransactionRequest request) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Account not found: " + accountId));

        BigDecimal currentBalance = account.getBalance();

        BigDecimal newBalance;

        if (request.getType() == TransactionType.CREDIT) {
            newBalance = currentBalance.add(request.getAmount());
        } else {
            newBalance = currentBalance.subtract(request.getAmount());
        }

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient account balance");
        }

        account.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());

        return transactionRepository.save(transaction);
    }
}