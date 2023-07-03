package com.example.moneyapp2.model.dto.income;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditIncomeDTO {

    private Long id;

    private BigDecimal amount;

    private String description;

    private LocalDateTime createdOn;

    private String incomeCategory;
}
