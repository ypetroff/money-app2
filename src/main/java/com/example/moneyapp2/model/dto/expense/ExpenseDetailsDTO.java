package com.example.moneyapp2.model.dto.expense;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    private String timeOfPurchase;

    private String category;
}
