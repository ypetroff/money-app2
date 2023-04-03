package com.example.moneyapp2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class AccountDashboardDTO {

    private BigDecimal cash;

    private BigDecimal card;

    private BigDecimal credit;
}
