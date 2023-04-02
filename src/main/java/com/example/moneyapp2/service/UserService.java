package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.UsernameOrPasswordDontMatchException;
import com.example.moneyapp2.model.dto.user.UserForAdminPanelDTO;
import com.example.moneyapp2.model.dto.user.UserInfoDTO;
import com.example.moneyapp2.model.dto.user.UserLoginDTO;
import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
//import com.example.moneyapp2.model.entity.user.MoneyAppUserDetails;
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

    public boolean isUserRepositoryEmpty() {
        return this.userRepository.count() == 0;
    }

    public void saveUserToDB(UserEntity user) {
        this.userRepository.saveAndFlush(user);
    }

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

    public void checkLoginCredentials(UserLoginDTO userLoginDTO) {

        if (!userRepository.findByUsername(userLoginDTO.getUsername())
                .map(userEntity -> passwordEncoder.matches(userLoginDTO.getPassword(), userEntity.getPassword()))
                .orElse(false)) {
            throw new UsernameOrPasswordDontMatchException("Username or password don't match");
        }
    }

    public Long getTotalNumberOfAppUsers() {
        return this.userRepository.count();
    }

    public List<UserInfoDTO> getAllAppUsers() {

        return this.userRepository.findAll().stream().map(this::mapUserEntityToUserInfoDTO).toList();
    }

    private UserInfoDTO mapUserEntityToUserInfoDTO(UserEntity user) {

        BigDecimal userTotalCashFunds = this.userRepository.userCashSum(user.getId())
                .orElse(BigDecimal.ZERO);
        BigDecimal userTotalCardFunds = this.userRepository.userCardSum(user.getId())
                .orElse(BigDecimal.ZERO);
        BigDecimal userTotalCredit = this.userRepository.findCreditsAmountById(user.getId())
                .orElse(BigDecimal.ZERO);
        List<String> roles = user.getUserRoles().stream()
                .map(role -> role.getUserRole().name())
                .toList();

        return UserInfoDTO.builder()
                .id(user.getId())
                .userDebit(userTotalCashFunds.add(userTotalCardFunds))
                .userCredit(userTotalCredit)
                .roles(roles)
                .build();
    }


    public List<UserForAdminPanelDTO> getAllUsersForAdminPanel() {
        return this.userRepository.findAll().stream()
                .map(u -> this.modelMapper.map(u, UserForAdminPanelDTO.class))
                .toList();
    }
}
