package com.mohan.expense_tracker.entity;

import com.mohan.expense_tracker.enums.JournalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "journals",
        indexes = {
                @Index(
                        name = "idx_journals_command_id",
                        columnList = "command_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Unique identifier supplied by the client.
     *
     * Used for idempotent processing.
     */
    @Column(
            name = "command_id",
            nullable = false,
            unique = true,
            length = 100
    )
    private String commandId;

    /**
     * Currency applies to the complete journal.
     *
     * Example: INR, USD, EUR
     */
    @Column(
            nullable = false,
            length = 3
    )
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private JournalStatus status;

    @Column(
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @OneToMany(
            mappedBy = "journal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JournalEntry> entries = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();

        if (status == null) {
            status = JournalStatus.POSTED;
        }
    }

    public void addEntry(JournalEntry entry) {
        entries.add(entry);
        entry.setJournal(this);
    }
}