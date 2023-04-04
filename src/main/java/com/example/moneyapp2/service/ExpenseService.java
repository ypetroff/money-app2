package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.expense.CreateExpenseDTO;
import com.example.moneyapp2.model.dto.expense.CreateExpenseMandatoryFieldsDTO;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseCategoryService expenseCategoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public boolean isExpenseRepositoryEmpty() {
        return this.expenseRepository.count() == 0;
    }

    public BigDecimal getTotalExpenseOnTeApp() {
        return this.expenseRepository.allUsersExpenseSum().orElse(BigDecimal.ZERO);
    }

    public BigDecimal getExpensesOfUser(String username) {
        return this.expenseRepository.findByOwnerUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("Username based on Principal not found"))
                .stream()
                .map(ExpenseEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void createEntityAndSaveIt(CreateExpenseDTO addExpenseDTO, String username) {

        ExpenseEntity entity = this.modelMapper.map(addExpenseDTO, ExpenseEntity.class);

        setOwnerAndCategory(entity, username, addExpenseDTO.getCategory());

        this.expenseRepository.saveAndFlush(entity);
    }

    public void createEntityAndSaveIt(CreateExpenseMandatoryFieldsDTO addExpenseDTO, String username) {

        ExpenseEntity entity = this.modelMapper.map(addExpenseDTO, ExpenseEntity.class);

        setOwnerAndCategory(entity, username, addExpenseDTO.getCategory());

        this.expenseRepository.saveAndFlush(entity);
    }

    private void setOwnerAndCategory(ExpenseEntity entity, String username, String addExpenseDTO) {

        entity.setOwner(this.modelMapper.map(this.userService.findUser(username), UserEntity.class));

        entity.setCategory(this.expenseCategoryService.addCategory(addExpenseDTO));
    }


}
