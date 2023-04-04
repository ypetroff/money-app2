package com.example.moneyapp2.model.dto.expense;

import jakarta.validation.constraints.*;
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
public class CreateExpenseDTO {

    @Size(min = 2, max = 200)
    @NotEmpty
    private String name;

    @Digits(integer = 10, fraction = 2)
    @NotEmpty
    private BigDecimal pricePerUnit;

    @NotNull
    @Positive
    private Integer numberOfUnits;

    @Digits(integer = 10, fraction = 2)
    @NotEmpty
    private BigDecimal totalPrice;

    @PastOrPresent
    @NotEmpty
    private LocalDateTime timeOfPurchase;

    @NotEmpty
    private String category;
}
