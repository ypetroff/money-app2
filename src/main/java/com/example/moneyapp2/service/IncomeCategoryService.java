package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.exception.NoSuchCategoryException;
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

    public IncomeCategoryEntity addCategory(String category) {

        try {
            return this.incomeCategoryRepository.findByCategory(IncomeCategory.valueOf(category))
                    .orElseThrow(() -> new NoAvailableDataException
                            ("There's no such income category in the repository"));
        } catch (IllegalArgumentException exception) {
            throw new NoSuchCategoryException("There's no such income category");
        }
    }

    public List<String> categoriesToString() {

        return this.incomeCategoryRepository.findAll().stream()
                .map(IncomeCategoryEntity::getCategory)
                .map(IncomeCategory::name)
                .toList();
    }
}
