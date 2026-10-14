package com.mohan.expense_tracker.controller;

import com.mohan.expense_tracker.dto.CreateJournalRequest;
import com.mohan.expense_tracker.entity.Journal;
import com.mohan.expense_tracker.service.LedgerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/journals")
public class JournalController {

    private final LedgerService ledgerService;

    public JournalController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Journal createJournal(
            @Valid @RequestBody CreateJournalRequest request) {

        return ledgerService.createJournal(request);
    }
}