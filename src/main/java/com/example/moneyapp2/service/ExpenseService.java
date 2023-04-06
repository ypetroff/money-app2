package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.expense.*;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseCategoryService expenseCategoryService;

    private final UserService userService;

    private final ModelMapper modelMapper;

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
        entity.setTimeOfPurchase(getTimeOfPurchase());

        this.expenseRepository.saveAndFlush(entity);
    }

    private void setOwnerAndCategory(ExpenseEntity entity, String username, String addExpenseDTO) {

        entity.setOwner(this.modelMapper.map(this.userService.findUser(username), UserEntity.class));
        entity.setCategory(this.expenseCategoryService.addCategory(addExpenseDTO));
    }

    private static LocalDateTime getTimeOfPurchase() {
        return LocalDateTime.now();
    }

    public List<ExpenseInfoDTO> getAllExpensesOfUser(String username) {

        return listOfExpenseInfoDTO(username);
    }


    public Object getDetailsOfExpense(Long id) {

        ExpenseEntity entity = this.expenseRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(
                        String.format("Expense with id: %d not found", id)));

        if (entity.getPricePerUnit() == null || entity.getNumberOfUnits() == null) {

            return this.modelMapper.map(entity, ExpenseMandatoryFieldsDetailsDTO.class);
        }

        return this.modelMapper.map(entity, ExpenseDetailsDTO.class);
    }

    public List<ExpenseInfoDTO> addNewExpenseAndReturnAllIncomeOfUser(CreateExpenseDTO expenseDTO,
                                                                      String username) {
        createEntityAndSaveIt(expenseDTO, username);

        return listOfExpenseInfoDTO(username);
    }

    public List<ExpenseInfoDTO> addNewExpenseAndReturnAllIncomeOfUser(CreateExpenseMandatoryFieldsDTO expenseDTO,
                                                                      String username) {
        createEntityAndSaveIt(expenseDTO, username);

        return listOfExpenseInfoDTO(username);
    }

    private List<ExpenseInfoDTO> listOfExpenseInfoDTO(String username) {

        return this.expenseRepository.findByOwnerUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User don't have expenses"))
                .stream()
                .map(i -> this.modelMapper.map(i, ExpenseInfoDTO.class))
                .toList();
    }

    public ExpenseDetailsDTO editExpense(Long id, CreateExpenseDTO changedExpenseInfo) {

        ExpenseEntity entity = this.expenseRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(String.format("Expense with id: %d not found", id)));

        this.modelMapper.map(changedExpenseInfo, entity);

        this.expenseRepository.saveAndFlush(entity);

        return this.modelMapper.map(entity, ExpenseDetailsDTO.class);
    }

    public boolean expenseNotPresent(Long id) {
        return !this.expenseRepository.existsById(id);
    }

    public void deleteExpense(Long id) {

        this.expenseRepository.deleteById(id);
    }

    public EditExpenseDTO getSingleExpense(Long id) {

        return this.modelMapper.map(this.expenseRepository.findById(id)
                        .orElseThrow(() -> new NoAvailableDataException("Non existent expense")),
                EditExpenseDTO.class);
    }

    public boolean unauthorizedUser(Long id, String username) {

        ExpenseEntity entity = this.expenseRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException("Non existent expense"));

        return !entity.getOwner().getUsername().equals(username);
    }
}
