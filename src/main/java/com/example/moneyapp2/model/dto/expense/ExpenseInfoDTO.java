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
public class ExpenseInfoDTO {

    private Long id;

    private String name;

    private BigDecimal totalPrice;
}
