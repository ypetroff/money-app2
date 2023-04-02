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
            SELECT SUM(d.cash) FROM UserEntity u
            JOIN  u.debit d
            WHERE u.id = :id
            """)
    Optional<BigDecimal> userCashSum(Long id);


    @Query("""
            SELECT SUM(d.card) FROM UserEntity u
            JOIN  u.debit d
            WHERE u.id = :id
            """)
    Optional<BigDecimal> userCardSum(Long id);

    @Query("""
            SELECT SUM(c.amount) FROM UserEntity u
            JOIN u.credits c
            WHERE u.id = :id
                        """)
    Optional<BigDecimal> findCreditsAmountById(Long id);
}
