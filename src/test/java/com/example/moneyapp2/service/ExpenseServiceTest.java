package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.expense.*;
import com.example.moneyapp2.model.entity.ExpenseCategoryEntity;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        toTest = new ExpenseService(this.mockExpenseRepository, this.mockExpenseCategoryService,
                                    this.mockUserService, this.mockModelMapper);
    }

    @Test
    void getTotalExpenseOnTeAppWithUsers() {

        Optional<BigDecimal> expectedSum = Optional.of(BigDecimal.TEN);

        when(this.mockExpenseRepository.allUsersExpenseSum())
                .thenReturn(expectedSum);

        BigDecimal actualSum = toTest.getTotalExpenseOnTeApp();

        assertEquals(expectedSum.get(), actualSum);
    }

    @Test
    void getTotalExpenseOnTeAppWithoutUsers() {

        BigDecimal actualSum = toTest.getTotalExpenseOnTeApp();

        assertEquals(BigDecimal.ZERO, actualSum);
    }

    @Test
    void getExpensesOfExistingUser() {

        UserEntity testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("test");

        ExpenseEntity testExpense = ExpenseEntity.builder()
                .totalPrice(BigDecimal.TEN)
                .owner(testUser)
                .build();

        List<ExpenseEntity> expenses = new ArrayList<>();
        expenses.add(testExpense);

        when(this.mockUserService.findUserEntity(testUser.getUsername()))
                .thenReturn(testUser);
        when(this.mockExpenseRepository.findByOwnerUsername(testUser.getUsername()))
                .thenReturn(Optional.of(expenses));

        assertEquals(testExpense.getTotalPrice(), toTest.getExpensesOfUser(testUser.getUsername()));
    }

    @Test
    void getExpensesOfExistingUserWithoutExpenses() {

        UserEntity testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("test");

        List<ExpenseEntity> expenses = new ArrayList<>();

        when(this.mockUserService.findUserEntity(testUser.getUsername()))
                .thenReturn(testUser);
        when(this.mockExpenseRepository.findByOwnerUsername(testUser.getUsername()))
                .thenReturn(Optional.of(expenses));

        assertEquals(BigDecimal.ZERO, toTest.getExpensesOfUser(testUser.getUsername()));
    }

    @Test
    void getExpensesOfNonExistingUser() {

        String testUsername = "test";

        when(this.mockUserService.findUserEntity(testUsername))
                .thenReturn(new UserEntity());

        assertThrows(NoAvailableDataException.class, () -> toTest.getExpensesOfUser(testUsername));
    }

    @Test
    void createEntityAndSaveIt() {

        CreateExpenseDTO expenseDTO = CreateExpenseDTO.builder()
                .name("name")
                .category("CAR")
                .numberOfUnits(1)
                .pricePerUnit(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .build();

        String username = "test";


        when(this.mockModelMapper.map(expenseDTO, ExpenseEntity.class))
                .thenReturn(ExpenseEntity.builder()
                        .name(expenseDTO.getName())
                        .category(new ExpenseCategoryEntity(ExpenseCategory.valueOf(expenseDTO.getCategory())))
                        .numberOfUnits(expenseDTO.getNumberOfUnits())
                        .pricePerUnit(expenseDTO.getPricePerUnit())
                        .totalPrice(expenseDTO.getTotalPrice())
                        .build());


        toTest.createEntityAndSaveIt(expenseDTO, username);
        verify(this.mockExpenseRepository).saveAndFlush(any());

    }

    @Test
    void createEntityAndSaveItWithMandatoryFields() {

        CreateExpenseMandatoryFieldsDTO expenseDTO = CreateExpenseMandatoryFieldsDTO.builder()
                .name("name")
                .category("CAR")
                .totalPrice(BigDecimal.TEN)
                .build();

        String username = "test";


        when(this.mockModelMapper.map(expenseDTO, ExpenseEntity.class))
                .thenReturn(ExpenseEntity.builder()
                        .name(expenseDTO.getName())
                        .category(new ExpenseCategoryEntity(ExpenseCategory.valueOf(expenseDTO.getCategory())))
                        .totalPrice(expenseDTO.getTotalPrice())
                        .build());


        toTest.createEntityAndSaveIt(expenseDTO, username);
        verify(this.mockExpenseRepository).saveAndFlush(any());

    }

    @Test
    void getAllExpensesOfUser() {

        UserEntity testUser = UserEntity.builder()
                .username("test")
                .build();

        ExpenseEntity testExpense = ExpenseEntity.builder()
                .name("test expense")
                .category(this.mockExpenseCategoryService.addCategory("HOME"))
                .totalPrice(BigDecimal.TEN)
                .timeOfPurchase(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
                .owner(testUser)
                .build();
        testExpense.setId(1L);

        ExpenseInfoDTO dtoExpense = ExpenseInfoDTO.builder()
                .id(testExpense.getId())
                .name(testExpense.getName())
                .totalPrice(testExpense.getTotalPrice())
                .build();

        doReturn(Optional.of(
                List.of(testExpense)))
                .when(this.mockExpenseRepository).findByOwnerUsername(testUser.getUsername());

        when(this.mockModelMapper.map(testExpense, ExpenseInfoDTO.class))
                .thenReturn(dtoExpense);

        List<ExpenseInfoDTO> listOfExpensesFromMethod = toTest.getAllExpensesOfUser("test");

        assertEquals(1, listOfExpensesFromMethod.size());

        ExpenseInfoDTO actual = listOfExpensesFromMethod.get(0);

        assertEquals(testExpense.getId(),actual.getId());
        assertEquals(testExpense.getTotalPrice(), actual.getTotalPrice());
        assertEquals(testExpense.getName(), actual.getName());
    }

    @Test
    void getAllExpensesOfUserWithoutExpenses() {

        UserEntity testUser = UserEntity.builder()
                .username("test")
                .build();

        List<ExpenseEntity> entities = new ArrayList<>();

        doReturn(Optional.of(entities))
                .when(this.mockExpenseRepository).findByOwnerUsername(testUser.getUsername());


        List<ExpenseInfoDTO> listOfExpensesFromMethod = toTest.getAllExpensesOfUser("test");

        assertEquals(0, listOfExpensesFromMethod.size());
    }

    @Test
    void getDetailsOfExpenseMandatoryFields() {
        ExpenseEntity testExpense = ExpenseEntity.builder()
                .name("test expense")
                .category(new ExpenseCategoryEntity(ExpenseCategory.HOME))
                .totalPrice(BigDecimal.TEN)
                .timeOfPurchase(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
                .build();
        testExpense.setId(1L);

        when(this.mockExpenseRepository.findById(1L))
                .thenReturn(Optional.of(testExpense));
        when(this.mockModelMapper.map(testExpense, ExpenseMandatoryFieldsDetailsDTO.class))
                .thenReturn(ExpenseMandatoryFieldsDetailsDTO.builder()
                        .category(testExpense.getCategory().toString())
                        .name(testExpense.getName())
                        .timeOfPurchase(testExpense.getTimeOfPurchase())
                        .totalPrice(testExpense.getTotalPrice())
                        .build());

        ExpenseMandatoryFieldsDetailsDTO detailsOfExpense =
                (ExpenseMandatoryFieldsDetailsDTO) this.toTest.getDetailsOfExpense(testExpense.getId());

        assertEquals(testExpense.getTotalPrice(), detailsOfExpense.getTotalPrice());
        assertEquals(testExpense.getName(), detailsOfExpense.getName());
        assertEquals(testExpense.getCategory().toString(), detailsOfExpense.getCategory());
    }

    @Test
    void getDetailsOfExpenseAllFields() {
        ExpenseEntity testExpense = ExpenseEntity.builder()
                .name("test expense")
                .category(new ExpenseCategoryEntity(ExpenseCategory.HOME))
                .numberOfUnits(1)
                .pricePerUnit(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .timeOfPurchase(LocalDateTime.of(1999, 3, 23, 12, 0, 0))
                .build();
        testExpense.setId(1L);

        when(this.mockExpenseRepository.findById(1L))
                .thenReturn(Optional.of(testExpense));
        when(this.mockModelMapper.map(testExpense, ExpenseDetailsDTO.class))
                .thenReturn(ExpenseDetailsDTO.builder()
                        .category(testExpense.getCategory().toString())
                        .name(testExpense.getName())
                        .numberOfUnits(testExpense.getNumberOfUnits())
                        .pricePerUnit(testExpense.getPricePerUnit())
                        .timeOfPurchase(testExpense.getTimeOfPurchase())
                        .totalPrice(testExpense.getTotalPrice())
                        .build());

        ExpenseDetailsDTO detailsOfExpense =
                (ExpenseDetailsDTO) this.toTest.getDetailsOfExpense(testExpense.getId());

        assertEquals(testExpense.getNumberOfUnits(), detailsOfExpense.getNumberOfUnits());
        assertEquals(testExpense.getPricePerUnit(), detailsOfExpense.getPricePerUnit());
        assertEquals(testExpense.getTotalPrice(), detailsOfExpense.getTotalPrice());
        assertEquals(testExpense.getName(), detailsOfExpense.getName());
        assertEquals(testExpense.getCategory().toString(), detailsOfExpense.getCategory());
    }

    @Test
    void getDetailsOfThrowsIfIdIsInvalid() {
        long id = 1;

        assertThrows(NoAvailableDataException.class, () -> toTest.getDetailsOfExpense(id));
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