package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
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

        String username = "test";
        when(mockIncomeService.getIncomeOfUser(username))
                .thenReturn(expectedIncome);
        when(mockExpenseService.getExpensesOfUser(username))
                .thenReturn(expectedExpense);

        AccountDashboardDTO userAccountInfo = toTest.getUserAccountInfo(username);

        Assertions.assertNotNull(userAccountInfo);
        Assertions.assertEquals(expectedIncome, userAccountInfo.getIncome());
        Assertions.assertEquals(expectedExpense, userAccountInfo.getExpenses());

    }

    @Test
    void testGetUserAccountInfo_UserDontExists() {

        String username = "test2";
        when(mockIncomeService.getIncomeOfUser(username))
                .thenThrow(NoAvailableDataException.class);

        Assertions.assertThrows(NoAvailableDataException.class,
                () -> toTest.getUserAccountInfo(username));
    }
}