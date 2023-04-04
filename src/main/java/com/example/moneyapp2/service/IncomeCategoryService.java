package com.example.moneyapp2.service;

import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.IncomeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;

    public boolean isUserIncomeCategoryRepositoryEmpty() {

        return this.incomeCategoryRepository.count() == 0;
    }

    public void saveCategoryToDB(IncomeCategory incomeCategory) {

        IncomeCategoryEntity entity = new IncomeCategoryEntity();
        entity.setCategory(incomeCategory);

        this.incomeCategoryRepository.saveAndFlush(entity);
    }
}
