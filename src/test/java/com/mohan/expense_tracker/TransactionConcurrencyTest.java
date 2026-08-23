package com.mohan.expense_tracker;

import com.mohan.expense_tracker.dto.transaction.CreateTransactionRequest;
import com.mohan.expense_tracker.entity.Account;
import com.mohan.expense_tracker.entity.User;
import com.mohan.expense_tracker.enums.SupportedCurrency;
import com.mohan.expense_tracker.enums.TransactionType;
import com.mohan.expense_tracker.enums.UserStatus;
import com.mohan.expense_tracker.repository.AccountRepository;
import com.mohan.expense_tracker.repository.UserRepository;
import com.mohan.expense_tracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TransactionConcurrencyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private Account account;

    @BeforeEach
    void setUp() {

        User user = new User();

        user.setName("Concurrency User");
        user.setEmail("concurrency@test.com");
        user.setPasswordHash("test-password");
        user.setPreferredCurrency(SupportedCurrency.INR);
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.saveAndFlush(user);

        Account newAccount = new Account();

        newAccount.setUser(user);
        newAccount.setName("Concurrency Account");
        newAccount.setBalance(new BigDecimal("10000"));
        newAccount.setCurrency(SupportedCurrency.INR);

        account = accountRepository.saveAndFlush(newAccount);
    }

    @Test
    void shouldDetectConcurrentAccountUpdate()
            throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch startLatch =
                new CountDownLatch(1);

        CountDownLatch finishLatch =
                new CountDownLatch(2);

        boolean[] optimisticLockFailure = {false};

        executor.submit(() -> {

            try {
                try {
                    startLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                transactionTemplate.executeWithoutResult(status -> {

                    Account currentAccount =
                            accountRepository
                                    .findById(account.getId())
                                    .orElseThrow();

                    currentAccount.setBalance(
                            currentAccount.getBalance()
                                    .subtract(new BigDecimal("1000"))
                    );

                    accountRepository.saveAndFlush(currentAccount);
                });

            } finally {
                finishLatch.countDown();
            }
        });

        executor.submit(() -> {

            try {
                startLatch.await();

                transactionTemplate.executeWithoutResult(status -> {

                    Account currentAccount =
                            accountRepository
                                    .findById(account.getId())
                                    .orElseThrow();

                    currentAccount.setBalance(
                            currentAccount.getBalance()
                                    .subtract(new BigDecimal("2000"))
                    );

                    accountRepository.saveAndFlush(currentAccount);
                });

            } catch (ObjectOptimisticLockingFailureException | InterruptedException ex) {

                optimisticLockFailure[0] = true;

            } finally {
                finishLatch.countDown();
            }
        });

        startLatch.countDown();

        finishLatch.await();

        executor.shutdown();

        assertTrue(
                optimisticLockFailure[0],
                "Expected an optimistic locking conflict"
        );
    }
}