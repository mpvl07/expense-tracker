package com.mohan.expense_tracker.mapper;

import com.mohan.expense_tracker.dto.account.AccountResponse;
import com.mohan.expense_tracker.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);
}