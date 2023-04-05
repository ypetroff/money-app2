package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.IncomeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;

    public void saveCategoryToDB(IncomeCategory incomeCategory) {

        IncomeCategoryEntity entity = new IncomeCategoryEntity();
        entity.setCategory(incomeCategory);

        this.incomeCategoryRepository.saveAndFlush(entity);
    }

    public IncomeCategoryEntity addCategory(String category) {
        return this.incomeCategoryRepository.findByCategory(IncomeCategory.valueOf(category))
                .orElseThrow(() -> new NoAvailableDataException("There's no such role"));
    }

    public List<String> categoriesToString() {

        return this.incomeCategoryRepository.findAll().stream()
                .map(IncomeCategoryEntity::getCategory)
                .map(IncomeCategory::name)
                .toList();
    }
}
