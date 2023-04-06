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
    private BigDecimal amount;

    private String description;

    @PastOrPresent(message = "Date of creation should be in the past or the present date")
    private LocalDateTime createdOn;

    @NotEmpty(message = "Income category cannot be empty String or null")
    private String incomeCategory;
}
