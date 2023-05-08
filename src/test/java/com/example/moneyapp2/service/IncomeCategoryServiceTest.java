package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.exception.NoSuchCategoryException;
import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import com.example.moneyapp2.repository.IncomeCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncomeCategoryServiceTest {

    @Mock
    private IncomeCategoryRepository mockIncomeCategoryRepository;

    private IncomeCategoryService toTest;

    @BeforeEach
    void setUp() {
        toTest = new IncomeCategoryService(this.mockIncomeCategoryRepository);
    }

    @Test
    void addCategoryThatExists() {
        IncomeCategory category = IncomeCategory.BANK;

        when(this.mockIncomeCategoryRepository.findByCategory(category))
                .thenReturn(Optional.of(new IncomeCategoryEntity(category)));

        IncomeCategoryEntity incomeCategoryEntity = toTest.addCategory(category.name());

        assertEquals(category, incomeCategoryEntity.getCategory());

    }

    @Test
    void addCategoryThatDoesNotExistInTheEnum() {
        NoSuchCategoryException exception = assertThrows(NoSuchCategoryException.class,
                () -> toTest.addCategory("non-existent category"));
        assertEquals("There's no such income category", exception.getMessage());
    }

    @Test
    void addCategoryThatDoesNotExistInTheRepository() {

        IncomeCategory category = IncomeCategory.BANK;

        when(this.mockIncomeCategoryRepository.findByCategory(category))
                .thenReturn(Optional.empty());

        NoAvailableDataException exception = assertThrows(NoAvailableDataException.class,
                () -> toTest.addCategory(category.name()));
        assertEquals("There's no such income category in the repository", exception.getMessage());
    }

    @Test
    void categoriesToString() {
    }
}