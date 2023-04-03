package com.example.moneyapp2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreditDataDTO {

    private BigDecimal credit;
}
