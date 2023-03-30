package com.example.moneyapp2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private Long id;
    private BigDecimal totalFunds;
    private BigDecimal totalDebt;

    private List<String> roles;
}
