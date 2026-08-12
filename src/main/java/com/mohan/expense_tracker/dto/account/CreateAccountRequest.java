package com.mohan.expense_tracker.dto.account;

import com.mohan.expense_tracker.enums.SupportedCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank
    private String name;

    private BigDecimal initialBalance;

    private SupportedCurrency currency;
}