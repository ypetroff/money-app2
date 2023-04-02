package com.example.moneyapp2.service;

import com.example.moneyapp2.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public boolean isExpenseRepositoryEmpty() {
        return this.expenseRepository.count() == 0;
    }
}
