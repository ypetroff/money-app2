package com.example.moneyapp2.service;

import com.example.moneyapp2.repository.IncomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

    @Mock
    private IncomeRepository mockIncomeRepository;

    @Mock
    private IncomeCategoryService mockIncomeCategoryService;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper mockModelMapper;

    private IncomeService toTest;
    @BeforeEach
    void setUp() {
        toTest = new IncomeService(this.mockIncomeRepository, this.mockIncomeCategoryService,
                this.mockUserService, this.mockModelMapper);
    }

    @Test
    void getTotalIncomeOnTheAppWithUser() {
        Optional<BigDecimal> expectedSum = Optional.of(BigDecimal.TEN);

        when(this.mockIncomeRepository.allIncomeSum())
                .thenReturn(expectedSum);

        BigDecimal actualSum = toTest.getTotalIncomeOnTheApp();

        assertEquals(expectedSum.get(), actualSum);
    }

    @Test
    void getTotalIncomeOnTheAppWithoutUsers() {

        BigDecimal actualSum = toTest.getTotalIncomeOnTheApp();

        assertEquals(BigDecimal.ZERO, actualSum);
    }

    @Test
    void getIncomeOfUser() {
    }

    @Test
    void addNewIncomeAndReturnAllIncomeOfUser() {
    }

    @Test
    void createEntityAndSaveIt() {
    }

    @Test
    void getAllIncomeOfUser() {
    }

    @Test
    void getDetailsOfIncome() {
    }

    @Test
    void editIncome() {
    }

    @Test
    void incomeNotPresent() {
    }

    @Test
    void deleteIncome() {
    }

    @Test
    void unauthorizedUser() {
    }

    @Test
    void getSingleIncome() {
    }

    @Test
    void maintenance() {
    }
}