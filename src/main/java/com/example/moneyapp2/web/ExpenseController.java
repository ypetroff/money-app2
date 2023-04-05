package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.expense.*;
import com.example.moneyapp2.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseInfoDTO>> allIncomeOfTheUser(Principal principal) {

        return ResponseEntity.ok(this.expenseService.getAllExpensesOfUser(principal.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<List<ExpenseInfoDTO>> addNewExpenseAllFields(@Valid @RequestBody CreateExpenseDTO expenseDTO,
                                                                       Principal principal) {

        return ResponseEntity.ok(this.expenseService.addNewExpenseAndReturnAllIncomeOfUser(expenseDTO,
                principal.getName()));
    }

    @PostMapping("/add-mandatory")
    public ResponseEntity<List<ExpenseInfoDTO>> addNewExpenseMandatoryFields(@Valid @RequestBody CreateExpenseMandatoryFieldsDTO expenseDTO,
                                                                             Principal principal) {

        return ResponseEntity.ok(this.expenseService.addNewExpenseAndReturnAllIncomeOfUser(expenseDTO,
                principal.getName()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> detailedExpenseInfo(@PathVariable Long id, Principal principal) {

        if (this.expenseService.expenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.expenseService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object detailsOfExpense = this.expenseService.getDetailsOfExpense(id);

        if (detailsOfExpense instanceof ExpenseMandatoryFieldsDetailsDTO) {
            return ResponseEntity.ok((ExpenseMandatoryFieldsDetailsDTO) detailsOfExpense);
        }

        return ResponseEntity.ok((ExpenseDetailsDTO) detailsOfExpense);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<EditExpenseDTO> getExpenseInfo(@PathVariable Long id, Principal principal) {

        if (this.expenseService.expenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.expenseService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.expenseService.getSingleExpense(id));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<ExpenseDetailsDTO> editExpenseInfo(@PathVariable Long id,
                                                             @Valid @RequestBody CreateExpenseDTO changedExpenseInfo,
                                                             Principal principal) {

        if (this.expenseService.expenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.expenseService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.expenseService.editExpense(id, changedExpenseInfo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id, Principal principal) {

        if (this.expenseService.expenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.expenseService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        this.expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
