package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {

    @Query("""
            SELECT SUM(c.amount)
            FROM CreditEntity c
            """)
    Optional<BigDecimal> allCreditSum();

}
