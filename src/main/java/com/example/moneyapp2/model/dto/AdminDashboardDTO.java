package com.example.moneyapp2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AdminDashboardDTO {

    public AdminDashboardDTO() {
        this.users = new ArrayList<>();
    }

    private Long totalUsersCount;

    private BigDecimal totalFundsOnTheApp;

    private BigDecimal totalDebtOnTheApp;

    private List<UserInfoDTO> users;


}
