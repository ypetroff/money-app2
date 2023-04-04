package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.expense.CreateExpenseDTO;
import com.example.moneyapp2.model.dto.expense.ExpenseDetailsDTO;
import com.example.moneyapp2.model.dto.expense.ExpenseInfoDTO;
import com.example.moneyapp2.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<ExpenseInfoDTO>> addNewExpense(@Valid @RequestBody CreateExpenseDTO expenseDTO,
                                                              Principal principal) {

        return ResponseEntity.ok(this.expenseService.addNewExpenseAndReturnAllIncomeOfUser(expenseDTO,
                                                                                           principal.getName()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ExpenseDetailsDTO> detailedExpenseInfo(@PathVariable Long id) {

        if(this.expenseService.ExpenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(this.expenseService.getDetailsOfExpense(id));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<ExpenseDetailsDTO> editExpenseInfo(@PathVariable Long id,
                                                             @Valid @RequestBody CreateExpenseDTO changedExpenseInfo) {

        if(this.expenseService.ExpenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(this.expenseService.editExpense(id, changedExpenseInfo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {

        if(this.expenseService.ExpenseNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        this.expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
