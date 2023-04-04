package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.expense.CreateExpenseDTO;
import com.example.moneyapp2.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<?> addExpense(@Valid @RequestBody CreateExpenseDTO expenseDTO, Principal principal) {

        this.expenseService.createEntityAndSaveIt(expenseDTO, principal.getName());

        return ResponseEntity.ok("Added");
    }

}
