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
    private final DebitService debitService;
    private final CreditService creditService;

    public AdminDashboardDTO provideAdminDashboardData() {

        Long totalNumberOfAppUsers = this.userService.getTotalNumberOfAppUsers();
        BigDecimal totalFundsInTheApp = this.debitService.getTotalDebitInTheApp();
        BigDecimal totalCreditInTheApp = this.creditService.getTotalCreditInTheApp();
        List<UserForAdminPanelDTO> users = this.userService.getAllUsersForAdminPanel();

        return AdminDashboardDTO.builder()
                .totalUsersCount(totalNumberOfAppUsers)
                .totalDebitOnTheApp(totalFundsInTheApp)
                .totalCreditOnTheApp(totalCreditInTheApp)
                .users(users)
                .build();
    }
}


