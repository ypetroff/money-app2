package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.entity.IncomeEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.IncomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
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
    void getIncomeOfUserWithIncome() {


        UserEntity user = UserEntity.builder()
                .username("test")
                .build();
        IncomeEntity income = IncomeEntity.builder()
                .amount(BigDecimal.TEN)
                .build();

        when(this.mockUserService.findUserEntity(user.getUsername()))
                .thenReturn(user);
        when(this.mockIncomeRepository.findByOwnerUsername(user.getUsername()))
                .thenReturn(Optional.of(List.of(income)));

        BigDecimal incomeOfUser = toTest.getIncomeOfUser(user.getUsername());
        assertEquals(income.getAmount(), incomeOfUser);
    }

    @Test
    void getIncomeOfUserWithoutIncome() {


        UserEntity user = UserEntity.builder()
                .username("test")
                .build();

        when(this.mockUserService.findUserEntity(user.getUsername()))
                .thenReturn(user);
        when(this.mockIncomeRepository.findByOwnerUsername(user.getUsername()))
                .thenReturn(Optional.of(List.of()));

        BigDecimal incomeOfUser = toTest.getIncomeOfUser(user.getUsername());
        assertEquals(BigDecimal.ZERO, incomeOfUser);
    }

    @Test
    void getIncomeOfInvalidUsername() {

        String username = "test";

        NoAvailableDataException exception = assertThrows(NoAvailableDataException.class,
                () -> toTest.getIncomeOfUser(username));
        assertEquals("User is not present in the database", exception.getMessage());
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