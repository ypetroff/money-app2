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
public class CreateExpenseMandatoryFieldsDTO {

    @Size(min = 2, max = 200, message = "Name should be between 2 and 200 characters")
    @NotEmpty(message = "Name cannot be empty string")
    private String name;

    @Digits(integer = 10, fraction = 2, message = "Price per Unit should be a valid number")
    @Positive(message = "Total price should be a positive number")
    private BigDecimal totalPrice;

    @PastOrPresent(message = "Time of purchase should be a date in the past or the present date")
    private LocalDateTime timeOfPurchase;

    @NotEmpty(message = "Category cannot be empty String or null")
    private String category;
}
