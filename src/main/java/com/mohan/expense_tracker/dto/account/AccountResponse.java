package com.mohan.expense_tracker.dto.account;

import com.mohan.expense_tracker.enums.SupportedCurrency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class AccountResponse {

    private UUID id;

    private String name;

    private BigDecimal balance;

    private SupportedCurrency currency;

    private Instant createdAt;

    private Instant updatedAt;
}