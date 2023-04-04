package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    @Query("""
            SELECT SUM(d.amount)
            FROM IncomeEntity d
            """)
    Optional<BigDecimal> allIncomeSum();

    Optional<List<IncomeEntity>> findByOwnerUsername(String username);
}
