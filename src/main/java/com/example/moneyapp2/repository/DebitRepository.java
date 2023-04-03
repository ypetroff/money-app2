package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.DebitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface DebitRepository extends JpaRepository<DebitEntity, Long> {

    @Query("""
            SELECT SUM(d.cash)
            FROM DebitEntity d
            """)
    Optional<BigDecimal> allDebitCashSum();

    @Query("""
            SELECT SUM(d.card)
            FROM DebitEntity d
            """)
    Optional<BigDecimal> allDebitCardSum();

    Optional<DebitEntity> findByOwnerUsername(String username);
}
