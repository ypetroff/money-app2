package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.DebitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitRepository extends JpaRepository<DebitEntity, Long> {
}
