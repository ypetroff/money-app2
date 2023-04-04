package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.enums.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategoryEntity, Long> {

    Optional<IncomeCategoryEntity> findByCategory(IncomeCategory category);
}
