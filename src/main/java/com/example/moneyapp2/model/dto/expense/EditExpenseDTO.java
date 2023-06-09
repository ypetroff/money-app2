package com.example.moneyapp2.model.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditExpenseDTO {

    private Long id;

    private String name;

    private BigDecimal pricePerUnit;

    private Integer numberOfUnits;

    private BigDecimal totalPrice;

    private String timeOfPurchase;

    private String category;
}
