package com.example.moneyapp2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDTO {

    private Long totalUsersCount;

    private BigDecimal totalFundsOnTheApp;

    private BigDecimal totalDebtOnTheApp;

    private List<UserInfoDTO> users;


}
