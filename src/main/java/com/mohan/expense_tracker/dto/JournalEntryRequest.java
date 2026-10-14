package com.mohan.expense_tracker.dto;

import com.mohan.expense_tracker.enums.EntryDirection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record JournalEntryRequest(

        @NotBlank
        String entityRef,

        @NotNull
        EntryDirection direction,

        @NotNull
        @DecimalMin(value = "0.0001")
        BigDecimal amount

) {
}