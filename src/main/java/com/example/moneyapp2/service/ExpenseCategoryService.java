package com.example.moneyapp2.service;

import com.example.moneyapp2.model.entity.ExpenseCategoryEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.repository.ExpenseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;

    public boolean isExpenseCategoryRepositoryEmpty() {
        return this.expenseCategoryRepository.count() == 0;
    }

    public void saveCategoryToDB(ExpenseCategory category) {

        ExpenseCategoryEntity entity = new ExpenseCategoryEntity();
        entity.setCategory(category);

        this.expenseCategoryRepository.saveAndFlush(entity);
    }
}
