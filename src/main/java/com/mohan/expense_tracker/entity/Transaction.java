package com.mohan.expense_tracker.entity;

import com.mohan.expense_tracker.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(
                        name = "idx_transactions_account_id",
                        columnList = "account_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 6)
    private TransactionType type;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}