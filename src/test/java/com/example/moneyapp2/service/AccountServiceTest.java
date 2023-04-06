package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.AccountDashboardDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private  final String TEST_USER_1 = "test";
    @Mock
    private IncomeService mockIncomeService;

    @Mock
    private ExpenseService mockExpenseService;

    private AccountService toTest;

    @BeforeEach
    void setUp() {
        toTest = new AccountService(mockIncomeService, mockExpenseService);
    }

    @Test
    void testGetUserAccountInfo_UserExists() {

        BigDecimal expectedIncome = BigDecimal.TEN;
        BigDecimal expectedExpense = BigDecimal.ONE;

        when(mockIncomeService.getIncomeOfUser(TEST_USER_1))
                .thenReturn(expectedIncome);
        when(mockExpenseService.getExpensesOfUser(TEST_USER_1))
                .thenReturn(expectedExpense);

        AccountDashboardDTO userAccountInfo = toTest.getUserAccountInfo(TEST_USER_1);

        Assertions.assertNotNull(userAccountInfo);
        Assertions.assertEquals(expectedIncome, userAccountInfo.getIncome());
        Assertions.assertEquals(expectedExpense, userAccountInfo.getExpenses());

    }
}