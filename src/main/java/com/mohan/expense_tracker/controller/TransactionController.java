package com.mohan.expense_tracker.controller;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.dto.transaction.TransactionResponse;
import com.mohan.expense_tracker.entity.Transaction;
import com.mohan.expense_tracker.mapper.TransactionMapper;
import com.mohan.expense_tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @PathVariable UUID accountId,
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction =
                transactionService.createTransaction(
                        accountId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.toResponse(transaction));
    }
}