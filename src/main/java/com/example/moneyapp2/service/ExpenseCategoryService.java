package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.entity.ExpenseCategoryEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.ExpenseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseCategoryEntity addCategory(String category) {

      return this.expenseCategoryRepository.findByCategory(ExpenseCategory.valueOf(category))
              .orElseThrow(() -> new NoAvailableDataException("There's no such role"));
    }

    public List<String> categoriesToString() {

        return this.expenseCategoryRepository.findAll().stream()
                .map(ExpenseCategoryEntity::getCategory)
                .map(ExpenseCategory::name)
                .toList();
    }
}
