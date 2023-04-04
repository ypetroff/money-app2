package com.example.moneyapp2.model.dto.income;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditIncomeDTO {

    @NotNull
    private Long id;

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
