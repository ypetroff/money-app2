package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query("""
            SELECT i.amount
            FROM UserEntity u
            JOIN u.income i
            WHERE u.id = :id
            """)
    Optional<BigDecimal> incomeSum(Long id);

    @Query("""
            SELECT e.totalPrice
            FROM UserEntity u
            JOIN u.expenses e
            WHERE u.id = :id
            """)
    Optional<BigDecimal> expensesSum(Long id);

    @Query("""
SELECT s.amount
FROM UserEntity u
JOIN u.savings s
WHERE u.id = :id
""")
    Optional<BigDecimal> savingsSum(Long id);
}
