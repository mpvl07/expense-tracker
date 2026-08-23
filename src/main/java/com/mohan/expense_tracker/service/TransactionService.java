package com.mohan.expense_tracker.service;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {

    Transaction createTransaction(
            UUID accountId,
            UUID userId,
            CreateTransactionRequest request,
            String idempotencyKey);

    Transaction getTransaction(
            UUID accountId,
            UUID transactionId,
            UUID userId);

    Page<Transaction> getTransactions(
            UUID accountId,
            UUID userId,
            Pageable pageable);
}