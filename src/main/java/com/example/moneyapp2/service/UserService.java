package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.user.UserForAdminPanelDTO;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.dto.user.UserInfoDTO;
import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean isEmailFreeToUse(String email) {
        return !this.userRepository.existsByEmail(email);
    }

    public boolean isUsernameFreeToUse(String username) {
        return !this.userRepository.existsByUsername(username);
    }

    public void registerUser(UserRegisterDTO userRegisterDTO) {

        this.userRepository.saveAndFlush(mapToUserEntity(userRegisterDTO));
    }

    private UserEntity mapToUserEntity(UserRegisterDTO userRegisterDTO) {

        UserEntity userEntity = this.modelMapper.map(userRegisterDTO, UserEntity.class);
        userEntity.addRole(this.userRoleService.getRole(UserRole.USER));
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return userEntity;
    }

    public void saveUserDtoToDB(UserForServicesDTO user) {

        saveEntityUserToDB(this.modelMapper.map(user, UserEntity.class));
    }

    public void saveEntityUserToDB(UserEntity user) {
        this.userRepository.saveAndFlush(user);
    }

    public UserEntity findUserEntity (String username) {

        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username"));
    }

    public UserForServicesDTO findUser(String username) {
        UserEntity entity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username"));

        return this.modelMapper.map(entity, UserForServicesDTO.class);
    }

    public Long getTotalNumberOfAppUsers() {
        return this.userRepository.count();
    }

    public List<UserForAdminPanelDTO> getAllUsersForAdminPanel() {
        return this.userRepository.findAll().stream()
                .map(u -> this.modelMapper.map(u, UserForAdminPanelDTO.class))
                .toList();
    }


    public boolean isNotPresent(Long id) {

        return !this.userRepository.existsById(id);
    }

    public void deleteUser(Long id) {

        this.userRepository.deleteById(id);
    }

    public void makeAdmin(Long id) {

        UserEntity entity = this.userRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException("User not found"));

        entity.addRole(this.userRoleService.getRole(UserRole.ADMIN));

        this.userRepository.saveAndFlush(entity);
    }

    public void removeAdminRights(Long id) {

        UserEntity entity = this.userRepository.findById(id)
                .orElseThrow(() -> new NoAvailableDataException("User not found"));

        entity.removeRole(this.userRoleService.getRole(UserRole.ADMIN));

        this.userRepository.saveAndFlush(entity);
    }
}
