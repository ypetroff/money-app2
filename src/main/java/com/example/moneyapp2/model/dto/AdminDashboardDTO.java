package com.example.moneyapp2.model.dto;

import com.example.moneyapp2.model.dto.user.UserForAdminPanelDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    private BigDecimal totalIncome;

    private BigDecimal totalExpenses;

    private List<UserForAdminPanelDTO> users;


}
