package com.example.moneyapp2.service;

import com.example.moneyapp2.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {


    @Mock
    private ExpenseRepository mockExpenseRepository;

    @Mock
    private ExpenseCategoryService mockExpenseCategoryService;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper mockModelMapper;

    private ExpenseService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ExpenseService(mockExpenseRepository, mockExpenseCategoryService,
                                    mockUserService, mockModelMapper);
    }

    @Test
    void getTotalExpenseOnTeAppWithUsers() {

        Optional<BigDecimal> expectedSum = Optional.of(BigDecimal.TEN);

        when(mockExpenseRepository.allUsersExpenseSum())
                .thenReturn(expectedSum);

        BigDecimal actualSum = toTest.getTotalExpenseOnTeApp();

        Assertions.assertEquals(expectedSum.get(), actualSum);
    }

    @Test
    void getTotalExpenseOnTeAppWithoutUsers() {

        BigDecimal actualSum = toTest.getTotalExpenseOnTeApp();

        Assertions.assertEquals(BigDecimal.ZERO, actualSum);
    }

    @Test
    void getExpensesOfUser() {
    }

    @Test
    void createEntityAndSaveIt() {
    }

    @Test
    void testCreateEntityAndSaveIt() {
    }

    @Test
    void getAllExpensesOfUser() {
    }

    @Test
    void getDetailsOfExpense() {
    }

    @Test
    void addNewExpenseAndReturnAllIncomeOfUser() {
    }

    @Test
    void testAddNewExpenseAndReturnAllIncomeOfUser() {
    }

    @Test
    void editExpense() {
    }

    @Test
    void expenseNotPresent() {
    }

    @Test
    void deleteExpense() {
    }

    @Test
    void getSingleExpense() {
    }

    @Test
    void unauthorizedUser() {
    }

    @Test
    void maintenance() {
    }
}