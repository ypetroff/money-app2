package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.entity.ExpenseCategoryEntity;
import com.example.moneyapp2.model.enums.ExpenseCategory;
import com.example.moneyapp2.repository.ExpenseCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryServiceTest {

    @Mock
    private ExpenseCategoryRepository mockExpenseCategoryRepository;

    private ExpenseCategoryService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ExpenseCategoryService(mockExpenseCategoryRepository);
    }

    @Test
    void testAddCategory_CategoryExists() {

        ExpenseCategoryEntity expectedEntity = new ExpenseCategoryEntity();
        expectedEntity.setCategory(ExpenseCategory.CAR);

        when(mockExpenseCategoryRepository.findByCategory(ExpenseCategory.CAR))
                .thenReturn(Optional.of(expectedEntity));

        ExpenseCategoryEntity actualEntity = toTest.addCategory(ExpenseCategory.CAR.name());

        Assertions.assertEquals(expectedEntity.getCategory().name(), actualEntity.getCategory().name());
    }

    @Test
    void testAddCategory_CategoryDoesNotExist() {

        Assertions.assertThrows(NoAvailableDataException.class,
                () -> toTest.addCategory(ExpenseCategory.CAR.name()));
    }

    @Test
    void testCategoriesToString_ReturnCategories() {

        ExpenseCategoryEntity entity1 = new ExpenseCategoryEntity();
        entity1.setCategory(ExpenseCategory.CAR);

        ExpenseCategoryEntity entity2 = new ExpenseCategoryEntity();
        entity2.setCategory(ExpenseCategory.SOCIAL_LIFE);

        ExpenseCategoryEntity entity3 = new ExpenseCategoryEntity();
        entity3.setCategory(ExpenseCategory.BEAUTY);

        List<ExpenseCategoryEntity> expectedEntities = List.of(entity1, entity2, entity3);

        when(mockExpenseCategoryRepository.findAll())
                .thenReturn(expectedEntities);

        List<String> actualEntities = toTest.categoriesToString();

        Assertions.assertEquals(expectedEntities.size(), actualEntities.size());
    }

    @Test
    void testCategoriesToString_DontReturnCategories() {

        Assertions.assertEquals(0, toTest.categoriesToString().size());
    }
}