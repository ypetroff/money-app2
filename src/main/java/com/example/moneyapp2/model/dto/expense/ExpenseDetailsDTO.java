package com.example.moneyapp2.model.dto.expense;

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
public class ExpenseDetailsDTO {

    private Long id;

    private String name;

    private BigDecimal pricePerUnit;

    private Integer numberOfUnits;

    private BigDecimal totalPrice;

    private LocalDateTime timeOfPurchase;

    private String category;
}
