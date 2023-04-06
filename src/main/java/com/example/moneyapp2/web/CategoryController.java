package com.example.moneyapp2.web;

import com.example.moneyapp2.service.ExpenseCategoryService;
import com.example.moneyapp2.service.IncomeCategoryService;
import com.example.moneyapp2.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final UserRoleService userRoleService;
    private final ExpenseCategoryService expenseCategoryService;
    private  final IncomeCategoryService incomeCategoryService;

    @GetMapping("/user-roles")
    public ResponseEntity<List<String>> getUserRoles() {

        return new ResponseEntity<>(this.userRoleService.categoriesToString(), HttpStatus.OK);
    }

    @GetMapping("/expense-category")
    public ResponseEntity<List<String>> getExpenseCategory() {

        return new ResponseEntity<>(this.expenseCategoryService.categoriesToString(), HttpStatus.OK);
    }

    @GetMapping("/income-category")
    public ResponseEntity<List<String>> getIncomeCategory() {

        return new ResponseEntity<>(this.incomeCategoryService.categoriesToString(), HttpStatus.OK);
    }
}
