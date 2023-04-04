package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.expense.CreateExpenseDTO;
import com.example.moneyapp2.model.dto.expense.CreateExpenseMandatoryFieldsDTO;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.model.enums.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class InitDB {
//
//    private static final String DEFAULT_PASSWORD = "12345";
//    private static final String ADMIN = "admin";
//    private static final String USER = "user";
//
//    private final UserRoleService userRoleService;
//    private final UserService userService;
//    private final IncomeCategoryService incomeCategoryService;
//    private final IncomeService incomeService;
//    private final ExpenseCategoryService expenseCategoryService;
//    private final ExpenseService expenseService;
//    private final SavingService savingService;
//    private final ModelMapper modelMapper;
//    private final PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void initTestUsersWithRolesAdminAndUser() {
//        initRoles();
//        initDemoUsers();
//        initIncomeCategories();
//        initDemoIncome();
//        initExpenseCategories();
////        initDemoExpenses();
////        initDemoSavings();
//    }
//
//    private void initRoles() {
//
//        if (this.userRoleService.isUserRoleRepositoryEmpty()) {
//            Arrays.stream(UserRole.values())
//                    .forEach(this.userRoleService::saveRoleToDB);
//        }
//    }
//
//    private void initDemoUsers() {
//
//        if (this.userService.isUserRepositoryEmpty()) {
//
//            UserRegisterDTO admin = initUser(ADMIN);
//            UserRegisterDTO user = initUser(USER);
//
//            Stream.of(admin, user)
//                    .map(dto -> this.modelMapper.map(dto, UserEntity.class))
//                    .peek(u -> u.setPassword(encodePassword(u.getPassword())))
//                    .peek(this::addRole)
//                    .forEach(this.userService::saveEntityUserToDB);
//        }
//    }
//
//    private void addRole(UserEntity user) {
//
//        List<UserRoleEntity> roles = new ArrayList<>();
//        roles.add(this.userRoleService.getRole(UserRole.USER));
//
//
//        if (user.getUsername().equalsIgnoreCase(UserRole.ADMIN.name())) {
//            roles.add(this.userRoleService.getRole(UserRole.ADMIN));
//        }
//
//        roles.forEach(user::addRole);
//    }
//
//    private String encodePassword(String password) {
//        return this.passwordEncoder.encode(password);
//    }
//
//    private UserRegisterDTO initUser(String role) {
//        return UserRegisterDTO.builder()
//                .username(role)
//                .firstName(role)
//                .lastName(role + "ov")
//                .password(DEFAULT_PASSWORD)
//                .email(role + "@example.com")
//                .build();
//    }
//
//    private void initIncomeCategories() {
//        if (this.incomeCategoryService.isUserIncomeCategoryRepositoryEmpty()) {
//            Arrays.stream(IncomeCategory.values())
//                    .forEach(this.incomeCategoryService::saveCategoryToDB);
//        }
//    }
//
//    private void initDemoIncome() {
//
//        if (this.incomeService.isIncomeRepositoryEmpty()) {
//
//            CreateIncomeDTO admin = CreateIncomeDTO.builder()
//                    .amount(BigDecimal.valueOf(10000.56))
//                    .incomeCategory(IncomeCategory.BANK.name())
//                    .description("Salary")
//                    .createdOn(LocalDateTime.now())
//                    .build();
//
//            CreateIncomeDTO user = CreateIncomeDTO.builder()
//                    .amount(BigDecimal.valueOf(900.56))
//                    .incomeCategory(IncomeCategory.CASH.name())
//                    .description("Gift")
//                    .createdOn(LocalDateTime.now())
//                    .build();
//
//            this.incomeService.createEntityAndSaveIt(admin, ADMIN);
//            this.incomeService.createEntityAndSaveIt(user, USER);
//        }
//    }
//
//    private void initExpenseCategories() {
//
//        if (this.expenseCategoryService.isExpenseCategoryRepositoryEmpty()) {
//            Arrays.stream(ExpenseCategory.values())
//                    .forEach(this.expenseCategoryService::saveCategoryToDB);
//        }
//    }
//
//    private void initDemoExpenses() {
//
//        if (this.expenseService.isExpenseRepositoryEmpty()) {
//
//            CreateExpenseDTO movieTickets = CreateExpenseDTO.builder()
//                    .name("Movie tickets")
//                    .numberOfUnits(2)
//                    .pricePerUnit(BigDecimal.valueOf(25))
//                    .totalPrice(BigDecimal.valueOf(25).pow(2))
//                    .category(ExpenseCategory.SOCIAL_LIFE.name())
//                    .timeOfPurchase(LocalDateTime.of(2023, Month.JANUARY, 23, 22, 22))
//                    .build();
//
//            this.expenseService.createEntityAndSaveIt(movieTickets, ADMIN);
//
//            CreateExpenseMandatoryFieldsDTO internetBill = CreateExpenseMandatoryFieldsDTO.builder()
//                    .name("Internet bill")
//                    .totalPrice(BigDecimal.valueOf(25))
//                    .category(ExpenseCategory.HOME.name())
//                    .timeOfPurchase(LocalDateTime.of(2023, Month.JANUARY, 23, 22, 22))
//                    .build();
//
//            this.expenseService.createEntityAndSaveIt(internetBill, USER);
//
//            CreateExpenseDTO tomatoes = CreateExpenseDTO.builder()
//                    .name("tomatoes")
//                    .numberOfUnits(2)
//                    .pricePerUnit(BigDecimal.valueOf(2.5))
//                    .totalPrice(BigDecimal.valueOf(2.5).pow(2))
//                    .category(ExpenseCategory.GROCERIES.name())
//                    .timeOfPurchase(LocalDateTime.of(2020, Month.JANUARY, 23, 22, 22))
//                    .build();
//
//            this.expenseService.createEntityAndSaveIt(tomatoes, USER);
//
//            CreateExpenseMandatoryFieldsDTO rent = CreateExpenseMandatoryFieldsDTO.builder()
//                    .name("rent")
//                    .totalPrice(BigDecimal.valueOf(800))
//                    .category(ExpenseCategory.HOME.name())
//                    .timeOfPurchase(LocalDateTime.of(2021, Month.JANUARY, 23, 9, 22))
//                    .build();
//
//            this.expenseService.createEntityAndSaveIt(rent, USER);
//
//            CreateExpenseMandatoryFieldsDTO holiday = CreateExpenseMandatoryFieldsDTO.builder()
//                    .name("holiday")
//                    .totalPrice(BigDecimal.valueOf(2800))
//                    .category(ExpenseCategory.FAMILY.name())
//                    .timeOfPurchase(LocalDateTime.of(2007, Month.JUNE, 13, 9, 0))
//                    .build();
//
//            this.expenseService.createEntityAndSaveIt(holiday, ADMIN);
//        }
//    }
//
//    private void initDemoSavings() {
//
//        CreateSavingDTO summerHolidayInGreece = CreateSavingDTO.builder()
//                .goal("Summer holiday in Greece")
//                .amount(BigDecimal.valueOf(10000))
//                .owners(List.of(ADMIN))
//                .contributors(List.of(ADMIN, USER))
//                .dateOfCreation((LocalDateTime.of(2022, Month.DECEMBER, 24, 21, 26)))
//                .endDate((LocalDate.of(2023, Month.APRIL, 9)))
//                .build();
//
//        this.savingService.createEntityAndSaveIt(summerHolidayInGreece);
//
//        CreateSavingDTO savingWithoutName = CreateSavingDTO.builder()
//                .amount(BigDecimal.valueOf(100))
//                .owners(List.of(USER))
//                .contributors(List.of(USER))
//                .dateOfCreation((LocalDateTime.of(2023, Month.FEBRUARY, 24, 21, 26)))
//                .endDate((LocalDate.of(2023, Month.APRIL, 9)))
//                .build();
//
//        this.savingService.createEntityAndSaveIt(savingWithoutName);
//
//        CreateSavingDTO rentForTheMonth = CreateSavingDTO.builder()
//                .goal("Rent for the  month")
//                .amount(BigDecimal.valueOf(10000))
//                .owners(List.of(ADMIN, USER))
//                .contributors(List.of(ADMIN, USER))
//                .dateOfCreation((LocalDateTime.of(2022, Month.MARCH, 9, 21, 26)))
//                .endDate((LocalDate.of(2023, Month.APRIL, 9)))
//                .build();
//
//        this.savingService.createEntityAndSaveIt(rentForTheMonth);
//    }
}
