package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.RegisterDTO;
import com.example.moneyapp2.model.entity.UserEntity;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRepository;
import com.example.moneyapp2.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class InitDB {

    private static final String DEFAULT_PASSWORD = "12345";

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initTestUsersWithRolesAdminAndUser() {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if(this.userRoleRepository.count() == 0) {
            Arrays.stream(UserRole.values())
                    .forEach(this.userRoleService::saveRoleToDB);
        }
    }

    private void initUsers() {
        if (this.userRepository.count() == 0) {
            RegisterDTO admin = initUser("admin");
            RegisterDTO user = initUser("user");

            Stream.of(admin, user)
                    .map(dto -> this.modelMapper.map(dto, UserEntity.class))
                    .peek(u -> u.setPassword(encodePassword(u.getPassword())))
                    .peek(this::addRole)
                    .forEach(this.userService::saveUserToDB);
        }
    }

    private void addRole(UserEntity user) {

        List<UserRoleEntity> roles = new ArrayList<>();
        roles.add(this.userRoleService.getRole(UserRole.USER));


        if (user.getUsername().equalsIgnoreCase(UserRole.ADMIN.name())) {
            roles.add(this.userRoleService.getRole(UserRole.ADMIN));
        }

        roles.forEach(user::addRole);
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    private RegisterDTO initUser(String role) {
        return RegisterDTO.builder()
                .username(role)
                .password(DEFAULT_PASSWORD)
                .email(role + "@example.com")
                .build();
    }
}
