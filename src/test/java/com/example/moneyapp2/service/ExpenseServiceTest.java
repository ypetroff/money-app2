package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.expense.*;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.entity.ExpenseCategoryEntity;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.repository.ExpenseCategoryRepository;
import com.example.moneyapp2.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void getTotalExpenseOnTeAppWithUser() {

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

        when(this.mockExpenseRepository.findByOwnerUsername(testUser.getUsername()))
                .thenReturn(Optional.of(expenses));

        assertEquals(BigDecimal.ZERO, toTest.getExpensesOfUser(testUser.getUsername()));
    }

    @Test
    void getExpensesOfNonExistingUser() {

        String username = "test";

        assertThrows(NoAvailableDataException.class, () -> toTest.getExpensesOfUser(username));
    }

    @Test
    void createEntityAndSaveItWithAllFields() {

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
        when(this.mockUserService.findUser(username))
                .thenReturn(new UserForServicesDTO());

        toTest.createEntityAndSaveIt(expenseDTO, username);
        verify(this.mockExpenseRepository).saveAndFlush(any());

    }

    @Test
    void createEntityAndSaveItWithAllFieldsThrowsUserNotFound() {

        String username = "test";
        String errorMessage = "test is not present in the database. The variable was extracted from the Principal";

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> toTest.createEntityAndSaveIt(new CreateExpenseDTO(), username));
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void createEntityAndSaveItWithMandatoryFieldsThrowsUserNotFound() {

        String username = "test";
        String errorMessage = "test is not present in the database. The variable was extracted from the Principal";

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> toTest.createEntityAndSaveIt(new CreateExpenseDTO(), username));
        assertEquals(errorMessage, exception.getMessage());
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
        when(this.mockUserService.findUser(username))
                .thenReturn(new UserForServicesDTO());


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
        when(this.mockUserService.findUser(testUser.getUsername()))
                .thenReturn(new UserForServicesDTO());

        List<ExpenseInfoDTO> listOfExpensesFromMethod = toTest.getAllExpensesOfUser("test");

        assertEquals(1, listOfExpensesFromMethod.size());

        ExpenseInfoDTO actual = listOfExpensesFromMethod.get(0);

        assertEquals(testExpense.getId(), actual.getId());
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
        when(this.mockUserService.findUser(testUser.getUsername()))
                .thenReturn(new UserForServicesDTO());

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
    void editExpenseWithValidId() {
        CreateExpenseDTO editInfo = CreateExpenseDTO.builder()
                .name("new_name")
                .category("CAR")
                .numberOfUnits(1)
                .pricePerUnit(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .timeOfPurchase(LocalDateTime.of(2000, 2,
                        22, 22, 22, 22))
                .build();

        ExpenseEntity entity = ExpenseEntity.builder()
                .name("original_name")
                .category(new ExpenseCategoryEntity(ExpenseCategory.CAR))
                .owner(new UserEntity())
                .numberOfUnits(2)
                .pricePerUnit(BigDecimal.ONE)
                .totalPrice(BigDecimal.TWO)
                .timeOfPurchase(LocalDateTime.of(2000, 2,
                        22, 22, 22, 22))
                .build();
        entity.setId(1L);

        when(this.mockExpenseRepository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            ExpenseEntity e = invocation.getArgument(1);
            e.setName(editInfo.getName());
            e.setCategory(new ExpenseCategoryEntity(ExpenseCategory.valueOf(editInfo.getCategory())));
            e.setNumberOfUnits(editInfo.getNumberOfUnits());
            e.setPricePerUnit(editInfo.getPricePerUnit());
            e.setTotalPrice(editInfo.getTotalPrice());
            e.setTimeOfPurchase(editInfo.getTimeOfPurchase());
            return null;
        })
                .when(this.mockModelMapper).map(editInfo, entity);

        doReturn(ExpenseDetailsDTO.builder()
                .id(entity.getId())
                .name(editInfo.getName())
                .category(editInfo.getCategory())
                .numberOfUnits(editInfo.getNumberOfUnits())
                .pricePerUnit(editInfo.getPricePerUnit())
                .totalPrice(editInfo.getTotalPrice())
                .timeOfPurchase(editInfo.getTimeOfPurchase())
                .build())
                .when(this.mockModelMapper).map(entity, ExpenseDetailsDTO.class);

        ExpenseDetailsDTO expenseDetailsDTO = toTest.editExpense(entity.getId(), editInfo);
        verify(this.mockExpenseRepository).findById(1L);
        verify(this.mockModelMapper).map(editInfo, entity);
        verify(this.mockExpenseRepository).saveAndFlush(entity);
        verify(this.mockModelMapper).map(entity, ExpenseDetailsDTO.class);

        assertEquals(editInfo.getName(), expenseDetailsDTO.getName());
        assertEquals(editInfo.getNumberOfUnits(), expenseDetailsDTO.getNumberOfUnits());
        assertEquals(editInfo.getPricePerUnit(), expenseDetailsDTO.getPricePerUnit());
        assertEquals(editInfo.getTotalPrice(), expenseDetailsDTO.getTotalPrice());
    }

    @Test
    void editExpense() {
        Long id = 1L;
        String errorMessage = String.format("Expense with id: %d not found", id);

        NoAvailableDataException exception =
                assertThrows(NoAvailableDataException.class, () -> toTest.editExpense(id, new CreateExpenseDTO()));
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void expenseNotPresentReturnsTrue() {
        assertTrue(toTest.expenseNotPresent(1L));
    }

    @Test
    void expenseNotPresentReturnsFalse() {
        when(this.mockExpenseRepository.existsById(1L))
                .thenReturn(true);

        assertFalse(toTest.expenseNotPresent(1L));
    }

    @Test
    void deleteExpense() {
        toTest.deleteExpense(1L);
        verify(this.mockExpenseRepository).deleteById(1L);
    }

    @Test
    void getSingleExpenseWithValidId() {
        long id = 1L;
        ExpenseEntity entity = ExpenseEntity.builder()
                .name("test")
                .category(new ExpenseCategoryEntity(ExpenseCategory.CAR))
                .owner(new UserEntity())
                .numberOfUnits(2)
                .pricePerUnit(BigDecimal.ONE)
                .totalPrice(BigDecimal.TWO)
                .timeOfPurchase(LocalDateTime.of(2000, 2,
                        22, 22, 22, 22))
                .build();
        entity.setId(id);
        EditExpenseDTO dto = EditExpenseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory().toString())
                .numberOfUnits(entity.getNumberOfUnits())
                .pricePerUnit(entity.getPricePerUnit())
                .totalPrice(entity.getTotalPrice())
                .timeOfPurchase(entity.getTimeOfPurchase().toString())
                .build();

        when(this.mockExpenseRepository.findById(id))
                .thenReturn(Optional.of(entity));
        when(this.mockModelMapper.map(entity, EditExpenseDTO.class))
                .thenReturn(dto);

        toTest.getSingleExpense(id);

        verify(this.mockExpenseRepository).findById(id);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getCategory().toString(), dto.getCategory());
        assertEquals(entity.getNumberOfUnits(), dto.getNumberOfUnits());
        assertEquals(entity.getPricePerUnit(), dto.getPricePerUnit());
        assertEquals(entity.getTotalPrice(), dto.getTotalPrice());
        assertEquals(entity.getTimeOfPurchase().toString(), dto.getTimeOfPurchase());
    }

    @Test
    void getSingleExpenseThrowsWithInvalidId() {
        assertThrows(NoAvailableDataException.class, () -> toTest.getSingleExpense(1L));
    }

    @Test
    void unauthorizedUserReturnsFalse() {
        String username = "username";
        long id = 1L;

        ExpenseEntity entity = ExpenseEntity.builder()
                .owner(UserEntity.builder()
                        .username(username)
                        .build())
                .build();

        when(this.mockUserService.findUser(username))
                .thenReturn(new UserForServicesDTO());
        when(this.mockExpenseRepository.findById(id))
                .thenReturn(Optional.of(entity));

        assertFalse(toTest.unauthorizedUser(id, username));
    }

    @Test
    void unauthorizedUserReturnsTrue() {
        String username = "username";
        long id = 1L;

        UserEntity user = UserEntity.builder()
                .username("test")
                .build();

        ExpenseEntity entity = ExpenseEntity.builder()
                .owner(user)
                .build();

        when(this.mockUserService.findUser(username))
                .thenReturn(new UserForServicesDTO());
        when(this.mockExpenseRepository.findById(id))
                .thenReturn(Optional.of(entity));

        assertTrue(toTest.unauthorizedUser(id, username));
    }

    @Test
    void unauthorizedUserThrowsUsernameNotFound() {
        String username = "username";
        long id = 1L;
        assertThrows(UsernameNotFoundException.class, () -> toTest.unauthorizedUser(id, username));
    }

    @Test
    void unauthorizedUserThrowsNoAvlData() {
        String username = "username";
        long id = 1L;

        when(this.mockUserService.findUser(username))
                .thenReturn(new UserForServicesDTO());

        assertThrows(NoAvailableDataException.class, () -> toTest.unauthorizedUser(id, username));
    }

    @Test
    void maintenance() {
        ExpenseEntity entityToRemain = ExpenseEntity.builder()
                .timeOfPurchase(LocalDateTime.of(2022, 2,
                        22, 22, 22, 22))
                .build();
        ExpenseEntity entityToBeDeleted = ExpenseEntity.builder()
                .timeOfPurchase(LocalDateTime.of(2000, 2,
                        22, 22, 22, 22))
                .build();

        when(this.mockExpenseRepository.findAll())
                .thenReturn(List.of(entityToRemain, entityToBeDeleted));

        toTest.maintenance();

        verify(this.mockExpenseRepository).findAll();
    }

    @Test
    void addNewExpenseAndReturnAllIncomeOfUserAllFields() {
        String username = "username";
        CreateExpenseDTO expenseDTO = CreateExpenseDTO.builder()
                .name("name")
                .category(ExpenseCategory.HEALTH.name())
                .numberOfUnits(1)
                .pricePerUnit(BigDecimal.ONE)
                .totalPrice(BigDecimal.ONE)
                .timeOfPurchase(LocalDateTime.of(2022, 2,
                        22, 22, 22, 22))
                .build();

        ExpenseEntity entity = ExpenseEntity.builder()
                .name("name")
                .category(new ExpenseCategoryEntity(ExpenseCategory.HEALTH))
                .owner(new UserEntity())
                .numberOfUnits(1)
                .pricePerUnit(BigDecimal.ONE)
                .totalPrice(BigDecimal.ONE)
                .timeOfPurchase(LocalDateTime.of(2022, 2,
                        22, 22, 22, 22))
                .build();
        UserForServicesDTO userForServicesDTO = UserForServicesDTO.builder()
                .id(1L)
                .username(username)
                .build();
        entity.setId(1L);

        doReturn(userForServicesDTO)
                .when(this.mockUserService).findUser(username);
        doReturn(entity)
                .when(this.mockModelMapper).map(expenseDTO, ExpenseEntity.class);
        doReturn(new UserEntity())
                .when(this.mockModelMapper).map(userForServicesDTO, UserEntity.class);
        when(this.mockExpenseCategoryService.addCategory(ExpenseCategory.HEALTH.name()))
                .thenReturn(new ExpenseCategoryEntity(ExpenseCategory.HEALTH));
        when(this.mockExpenseRepository.findByOwnerUsername(username))
                .thenReturn(Optional.of(
                        List.of(entity)));
        doReturn(ExpenseInfoDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalPrice(entity.getTotalPrice())
                .build())
                .when(this.mockModelMapper).map(entity, ExpenseInfoDTO.class);

        List<ExpenseInfoDTO> expenseInfoDTOS = toTest.addNewExpenseAndReturnAllIncomeOfUser(expenseDTO, username);
        assertEquals(1, expenseInfoDTOS.size());
        ExpenseInfoDTO actual = expenseInfoDTOS.get(0);
        assertEquals(entity.getId(), actual.getId());
        assertEquals(entity.getName(), actual.getName());
        assertEquals(entity.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    void addNewExpenseAndReturnAllIncomeOfUserMandatoryFields() {
        String username = "username";
        CreateExpenseMandatoryFieldsDTO expenseDTO = CreateExpenseMandatoryFieldsDTO.builder()
                .name("name")
                .category(ExpenseCategory.HEALTH.name())
                .totalPrice(BigDecimal.ONE)
                .timeOfPurchase(LocalDateTime.of(2022, 2,
                        22, 22, 22, 22))
                .build();

        ExpenseEntity entity = ExpenseEntity.builder()
                .name("name")
                .category(new ExpenseCategoryEntity(ExpenseCategory.HEALTH))
                .owner(new UserEntity())
                .numberOfUnits(1)
                .pricePerUnit(BigDecimal.ONE)
                .totalPrice(BigDecimal.ONE)
                .timeOfPurchase(LocalDateTime.of(2022, 2,
                        22, 22, 22, 22))
                .build();
        UserForServicesDTO userForServicesDTO = UserForServicesDTO.builder()
                .id(1L)
                .username(username)
                .build();
        entity.setId(1L);

        doReturn(userForServicesDTO)
                .when(this.mockUserService).findUser(username);
        doReturn(entity)
                .when(this.mockModelMapper).map(expenseDTO, ExpenseEntity.class);
        doReturn(new UserEntity())
                .when(this.mockModelMapper).map(userForServicesDTO, UserEntity.class);
        when(this.mockExpenseCategoryService.addCategory(ExpenseCategory.HEALTH.name()))
                .thenReturn(new ExpenseCategoryEntity(ExpenseCategory.HEALTH));
        when(this.mockExpenseRepository.findByOwnerUsername(username))
                .thenReturn(Optional.of(
                        List.of(entity)));
        doReturn(ExpenseInfoDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalPrice(entity.getTotalPrice())
                .build())
                .when(this.mockModelMapper).map(entity, ExpenseInfoDTO.class);

        List<ExpenseInfoDTO> expenseInfoDTOS = toTest.addNewExpenseAndReturnAllIncomeOfUser(expenseDTO, username);
        assertEquals(1, expenseInfoDTOS.size());
        ExpenseInfoDTO actual = expenseInfoDTOS.get(0);
        assertEquals(entity.getId(), actual.getId());
        assertEquals(entity.getName(), actual.getName());
        assertEquals(entity.getTotalPrice(), actual.getTotalPrice());
    }

}