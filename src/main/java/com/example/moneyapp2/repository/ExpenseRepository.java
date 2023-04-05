package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query("""
            SELECT SUM(e.totalPrice)
            FROM ExpenseEntity e
            """)
    Optional<BigDecimal> allUsersExpenseSum();

    @Query("""
            SELECT SUM(e.totalPrice)
            FROM ExpenseEntity e
            JOIN  e.owner o
            WHERE o.id = :id
            """)
    Optional<BigDecimal> userExpenseSum(Long id);

    Optional<List<ExpenseEntity>> findByOwnerUsername(String username);
}
