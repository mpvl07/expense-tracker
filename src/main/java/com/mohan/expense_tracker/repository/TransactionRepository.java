package com.mohan.expense_tracker.repository;

import com.mohan.expense_tracker.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
    Page<Transaction> findByAccountId(UUID accountId, Pageable pageable);
    Optional<Transaction> findByIdAndAccountId(
            UUID transactionId,
            UUID accountId
    );
}