package com.example.moneyapp2.model.dto.expense;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseMandatoryFieldsDetailsDTO {

    private String name;

    private BigDecimal totalPrice;

    private LocalDateTime timeOfPurchase;

    private String category;
}
