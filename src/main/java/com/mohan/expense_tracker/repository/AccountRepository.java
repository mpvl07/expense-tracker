package com.mohan.expense_tracker.repository;

import com.mohan.expense_tracker.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Page<Account> findByUserId(UUID userId, Pageable pageable);
}