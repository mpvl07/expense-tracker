package com.mohan.expense_tracker.mapper;

import com.mohan.expense_tracker.dto.transaction.TransactionResponse;
import com.mohan.expense_tracker.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction transaction);
}