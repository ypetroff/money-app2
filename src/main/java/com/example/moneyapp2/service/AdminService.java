package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.AdminDashboardDTO;
import com.example.moneyapp2.model.dto.user.UserForAdminPanelDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;

    private final IncomeService incomeService;

    private final ExpenseService expenseService;

    public AdminDashboardDTO provideAdminDashboardData() {

        Long totalNumberOfAppUsers = this.userService.getTotalNumberOfAppUsers();
        BigDecimal totalFundsOnTheApp = this.incomeService.getTotalIncomeOnTheApp();
        BigDecimal totalExpensesOnTheApp = this.expenseService.getTotalExpenseOnTeApp();
        List<UserForAdminPanelDTO> users = this.userService.getAllUsersForAdminPanel();

        return AdminDashboardDTO.builder()
                .totalUsersCount(totalNumberOfAppUsers)
                .totalIncome(totalFundsOnTheApp)
                .totalExpenses(totalExpensesOnTheApp)
                .users(users)
                .build();
    }
}


