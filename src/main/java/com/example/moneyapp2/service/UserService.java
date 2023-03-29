package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.MappingNotSuccessfulException;
import com.example.moneyapp2.model.dto.UserRegisterDTO;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public void saveUserToDB(UserEntity user) {
        this.userRepository.saveAndFlush(user);
    }

    public boolean isEmailFreeToUse(String email) {
        return this.userRepository.existsByEmailNot(email);
    }

    public boolean isUsernameFreeToUse(String username) {
        return this.userRepository.existsByUsernameNot(username);
    }

    public void registerUser(UserRegisterDTO userRegisterDTO) {
        this.userRepository.saveAndFlush(mapToUserEntity(userRegisterDTO));
    }

    private UserEntity mapToUserEntity(UserRegisterDTO userRegisterDTO) throws MappingNotSuccessfulException("Mapping UserRegisterDTO to UserEntity not successful") {

        UserEntity userEntity = this.modelMapper.map(userRegisterDTO, UserEntity.class);
        userEntity.addRole(this.userRoleService.getRole(UserRole.USER));
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return userEntity;
    }
}
