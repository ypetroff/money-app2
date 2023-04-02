package com.example.moneyapp2.service;

import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public  boolean isUserRoleRepositoryEmpty() {
        return this.userRoleRepository.count() == 0;
    }

    public void saveRoleToDB(UserRole role) {

        UserRoleEntity entity = new UserRoleEntity();
        entity.setUserRole(role);

        this.userRoleRepository.saveAndFlush(entity);
    }

    public UserRoleEntity getRole(UserRole role) {
        Optional<UserRoleEntity> userRole = this.userRoleRepository.findByUserRole(role);

        return userRole.orElseGet(UserRoleEntity::new); //todo: custom exception
    }
}
