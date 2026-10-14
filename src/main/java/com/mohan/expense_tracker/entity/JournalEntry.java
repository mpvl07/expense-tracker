package com.mohan.expense_tracker.entity;

import com.mohan.expense_tracker.enums.EntryDirection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "journal_entries",
        indexes = {
                @Index(
                        name = "idx_journal_entries_journal_id",
                        columnList = "journal_id"
                ),
                @Index(
                        name = "idx_journal_entries_entity_ref",
                        columnList = "entity_ref"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "journal_id",
            nullable = false
    )
    private Journal journal;
    public void setJournal(Journal journal) {
        this.journal = journal;
    }
    /**
     * Generic reference owned by the client application.
     *
     * Ledger does not know whether this represents:
     * an account, order, wallet, customer, subscription, etc.
     */
    @Column(
            name = "entity_ref",
            nullable = false,
            length = 200
    )
    private String entityRef;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 6
    )
    private EntryDirection direction;

    /**
     * Monetary amount.
     *
     * BigDecimal is used because floating-point types
     * are not suitable for financial calculations.
     */
    @Column(
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal amount;
}