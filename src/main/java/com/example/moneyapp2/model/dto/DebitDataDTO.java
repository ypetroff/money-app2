package com.example.moneyapp2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DebitDataDTO {

    private BigDecimal cash;
    private BigDecimal card;
}
