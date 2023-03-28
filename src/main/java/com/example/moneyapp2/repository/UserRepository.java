package com.example.moneyapp2.repository;

import com.example.moneyapp2.model.entity.UserEntity;
import com.example.moneyapp2.security.ApplicationUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);

    Optional<UserEntity> findByEmail(String username);
}
