package com.mohan.expense_tracker.service;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.entity.Transaction;

import java.util.UUID;

public interface TransactionService {

    Transaction createTransaction(
            UUID accountId,
            CreateTransactionRequest request
    );
}