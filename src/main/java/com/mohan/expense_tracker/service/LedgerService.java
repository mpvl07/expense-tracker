package com.mohan.expense_tracker.service;

import com.mohan.expense_tracker.dto.CreateJournalRequest;
import com.mohan.expense_tracker.dto.JournalEntryRequest;
import com.mohan.expense_tracker.entity.Journal;
import com.mohan.expense_tracker.entity.JournalEntry;
import com.mohan.expense_tracker.enums.EntryDirection;
import com.mohan.expense_tracker.repository.JournalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LedgerService {

    private final JournalRepository journalRepository;

    public LedgerService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Transactional
    public Journal createJournal(CreateJournalRequest request) {

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        Journal journal = new Journal();
        journal.setCommandId(request.commandId());
        journal.setCurrency(request.currency());

        for (JournalEntryRequest requestEntry : request.entries()) {

            JournalEntry entry = new JournalEntry();

            entry.setEntityRef(requestEntry.entityRef());
            entry.setDirection(requestEntry.direction());
            entry.setAmount(requestEntry.amount());

            journal.addEntry(entry);

            if (requestEntry.direction() == EntryDirection.DEBIT) {
                totalDebit = totalDebit.add(requestEntry.amount());
            } else {
                totalCredit = totalCredit.add(requestEntry.amount());
            }
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalArgumentException(
                    "Total debit must equal total credit"
            );
        }

        return journalRepository.save(journal);
    }
}