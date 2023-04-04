package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.expense.CreateExpenseDTO;
import com.example.moneyapp2.model.dto.expense.CreateExpenseMandatoryFieldsDTO;
import com.example.moneyapp2.model.dto.expense.ExpenseDetailsDTO;
import com.example.moneyapp2.model.dto.expense.ExpenseInfoDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.model.entity.ExpenseEntity;
import com.example.moneyapp2.model.entity.IncomeEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public List<ExpenseInfoDTO> getAllExpensesOfUser(String username) {

            return listOfExpenseInfoDTO(username);
    }

    private void setOwnerAndCategory(ExpenseEntity entity, String username, String addExpenseDTO) {

        entity.setOwner(this.modelMapper.map(this.userService.findUser(username), UserEntity.class));

        entity.setCategory(this.expenseCategoryService.addCategory(addExpenseDTO));
    }


    public ExpenseDetailsDTO getDetailsOfExpense(Long id) {

       return this.modelMapper.map(this.expenseRepository.findById(id)
                        .orElseThrow(() -> new NoAvailableDataException(
                                String.format("Expense with id: %d not found", id))),
               ExpenseDetailsDTO.class);
    }

    public List<ExpenseInfoDTO> addNewExpenseAndReturnAllIncomeOfUser(CreateExpenseDTO expenseDTO,
                                                                                      String username) {
        createEntityAndSaveIt(expenseDTO, username);

        return listOfExpenseInfoDTO(username);
    }

    private List<ExpenseInfoDTO> listOfExpenseInfoDTO(String username) {

        return getByOwnerUsername(username).stream()
                .map(i -> this.modelMapper.map(i, ExpenseInfoDTO.class))
                .toList();
    }

    private Optional<List<ExpenseEntity>> getByOwnerUsername(String username) {
        return this.expenseRepository.findByOwnerUsername(username);
    }

    public ExpenseDetailsDTO editExpense(Long id, CreateExpenseDTO changedExpenseInfo) {

        ExpenseEntity entity = this.expenseRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException(String.format("Expense with id: %d not found", id)));

        this.modelMapper.map(changedExpenseInfo, entity);

        this.expenseRepository.saveAndFlush(entity);

        return this.modelMapper.map(entity, ExpenseDetailsDTO.class);
    }

    public boolean ExpenseNotPresent(Long id) {
        return !this.expenseRepository.existsById(id);
    }

    public void deleteExpense(Long id) {
        this.expenseRepository.deleteById(id);
    }
}
