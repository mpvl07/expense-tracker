package com.mohan.expense_tracker.dto.transaction;

import com.mohan.expense_tracker.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class TransactionResponse {

    private UUID id;

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private Instant createdAt;
}