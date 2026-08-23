package com.mohan.expense_tracker.controller;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.dto.transaction.TransactionResponse;
import com.mohan.expense_tracker.entity.Transaction;
import com.mohan.expense_tracker.mapper.TransactionMapper;
import com.mohan.expense_tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable UUID accountId,
            @PathVariable UUID transactionId) {

        UUID userId = getAuthenticatedUserId();

        Transaction transaction =
                transactionService.getTransaction(
                        accountId,
                        transactionId,
                        userId
                );

        return ResponseEntity.ok(
                transactionMapper.toResponse(transaction)
        );
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @PathVariable UUID accountId,
            @PageableDefault(size = 20)
            Pageable pageable) {

        UUID userId = getAuthenticatedUserId();

        Page<TransactionResponse> response =
                transactionService
                        .getTransactions(accountId, userId, pageable)
                        .map(transactionMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @PathVariable UUID accountId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateTransactionRequest request) {

        UUID userId = getAuthenticatedUserId();

        Transaction transaction =
                transactionService.createTransaction(
                        accountId,
                        userId,
                        request,
                        idempotencyKey
                );

        return ResponseEntity.ok(
                transactionMapper.toResponse(transaction)
        );
    }

    private UUID getAuthenticatedUserId() {

        return (UUID) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}