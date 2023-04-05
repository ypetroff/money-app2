package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.SavingEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsRepository extends JpaRepository<SavingEntity, Long> {

    Optional<List<SavingEntity>> findAllByOwnersContaining(UserEntity user);
}
