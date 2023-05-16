package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.AccountDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    public AccountDashboardDTO getUserAccountInfo(String username) {

        BigDecimal income = this.incomeService.getIncomeOfUser(username);
        BigDecimal expenses = this.expenseService.getExpensesOfUser(username);

        return AccountDashboardDTO.builder()
                .income(income)
                .expenses(expenses)
                .build();
    }
}
