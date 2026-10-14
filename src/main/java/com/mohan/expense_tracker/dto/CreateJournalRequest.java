package com.mohan.expense_tracker.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateJournalRequest(

        @NotBlank
        String commandId,

        @NotBlank
        @Size(min = 3, max = 3)
        String currency,

        @NotEmpty
        @Valid
        List<JournalEntryRequest> entries

) {
}