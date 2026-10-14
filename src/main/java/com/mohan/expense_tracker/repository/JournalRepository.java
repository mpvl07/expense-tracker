package com.mohan.expense_tracker.repository;

import com.mohan.expense_tracker.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JournalRepository extends JpaRepository<Journal, UUID> {

    Optional<Journal> findByCommandId(String commandId);

    boolean existsByCommandId(String commandId);
}