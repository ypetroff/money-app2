package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.model.enums.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class InitDB {

    private static final String DEFAULT_PASSWORD = "12345";

    private final UserRoleService userRoleService;
    private final UserService userService;
    private final ExpenseCategoryService expenseCategoryService;
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initTestUsersWithRolesAdminAndUser() {
        initRoles();
        initDemoUsers();
        initExpenseCategories();
        initDemoExpenses();
    }

    private void initRoles() {

        if(this.userRoleService.isUserRoleRepositoryEmpty()) {
            Arrays.stream(UserRole.values())
                    .forEach(this.userRoleService::saveRoleToDB);
        }
    }

    private void initDemoUsers() {

        if (this.userService.isUserRepositoryEmpty()) {
            UserRegisterDTO admin = initUser("admin");
            UserRegisterDTO user = initUser("user");

            Stream.of(admin, user)
                    .map(dto -> this.modelMapper.map(dto, UserEntity.class))
                    .peek(u -> u.setPassword(encodePassword(u.getPassword())))
                    .peek(this::addRole)
                    .forEach(this.userService::saveUserToDB);
        }
    }

    private void addRole(UserEntity user) {

        List<UserRoleEntity> roles = new ArrayList<>();
        roles.add(this.userRoleService.getRole(UserRole.USER));


        if (user.getUsername().equalsIgnoreCase(UserRole.ADMIN.name())) {
            roles.add(this.userRoleService.getRole(UserRole.ADMIN));
        }

        roles.forEach(user::addRole);
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    private UserRegisterDTO initUser(String role) {
        return UserRegisterDTO.builder()
                .username(role)
                .firstName(role)
                .lastName(role + "ov")
                .password(DEFAULT_PASSWORD)
                .email(role + "@example.com")
                .build();
    }

    private void initExpenseCategories() {

        if(this.expenseCategoryService.isExpenseCategoryRepositoryEmpty()) {
            Arrays.stream(ExpenseCategory.values())
                    .forEach(this.expenseCategoryService::saveCategoryToDB);
        }
    }

    private void initDemoExpenses() {

        if(this.expenseService.isExpenseRepositoryEmpty()) {
            //todo: init
        }

    }
}
