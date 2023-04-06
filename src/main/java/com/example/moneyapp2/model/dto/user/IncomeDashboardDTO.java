package com.example.moneyapp2.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDashboardDTO {

    private Long id;

    private BigDecimal amount;

    private String incomeCategory;
}
