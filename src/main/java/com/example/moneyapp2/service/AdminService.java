package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.AdminDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;
    private final DebitService debitService;

    public AdminDashboardDTO provideDashboardData() {

        Long totalNumberOfAppUsers = this.userService.getTotalNumberOfAppUsers();
        BigDecimal totalFundsInTheApp = this.debitService.getTotalFundsInTheApp();

        return AdminDashboardDTO.builder()
                .totalUsersCount(totalNumberOfAppUsers)
                .totalFundsOnTheApp(totalFundsInTheApp)
                .totalDebtOnTheApp() //todo: add debt functionality
                .users()
                .build();
    }
}


