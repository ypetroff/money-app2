package com.example.moneyapp2.model.dto.income;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDetailsDTO {

    private Long id;

    private BigDecimal amount;

    private String description;

    private String createdOn;

    private String incomeCategory;

}
