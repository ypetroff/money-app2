package com.example.moneyapp2.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    private Long id;

    private String username;

    private BigDecimal income;

    private BigDecimal expenses;

    private BigDecimal savings;
}
