package com.example.moneyapp2.model.dto.income;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateIncomeDTO {

    @Digits(integer = 10, fraction = 2)
    @Positive
    @NotEmpty
    private BigDecimal amount;

    private String description;

    @NotEmpty
    @PastOrPresent
    private LocalDateTime createdOn;

    @NotEmpty
    private String incomeCategory;
}
