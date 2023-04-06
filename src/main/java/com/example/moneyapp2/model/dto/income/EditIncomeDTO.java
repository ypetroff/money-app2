package com.example.moneyapp2.model.dto.income;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditIncomeDTO {

    private Long id;

    private BigDecimal amount;

    private String description;

    private LocalDateTime createdOn;

    private String incomeCategory;
}
