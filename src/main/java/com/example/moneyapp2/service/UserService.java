package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.exception.UsernameAlreadyTaken;
import com.example.moneyapp2.model.dto.user.*;
//import com.example.moneyapp2.model.entity.user.MoneyAppUserDetails;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private final AuthenticationManager authenticationManager;

    public boolean isUserRepositoryEmpty() {
        return this.userRepository.count() == 0;
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

    public UserProfileDTO updateUsername(String currentUsername, String newUsername) {

        if (!isUsernameFreeToUse(newUsername)) {
            throw new UsernameAlreadyTaken(String.format("Username %s is already taken!", newUsername));
        }

        UserEntity entity = this.userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username for user update"));

        entity.setUsername(newUsername);

        this.userRepository.saveAndFlush(entity);

        return provideUserProfileData(newUsername);
    }

    public void saveUserToDB(UserEntity user) {
        this.userRepository.saveAndFlush(user);
    }

    public Long getTotalNumberOfAppUsers() {
        return this.userRepository.count();
    }

    public List<UserInfoDTO> getAllAppUsers() {

        return this.userRepository.findAll().stream().map(this::mapUserEntityToUserInfoDTO).toList();
    }

    public UserInfoDTO provideUserDashboardData(String username) {

        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username for user dashboard"));

        return mapUserEntityToUserInfoDTO(user);
    }

    private UserInfoDTO mapUserEntityToUserInfoDTO(UserEntity user) {

        BigDecimal userTotalCashFunds = this.userRepository.userCashSum(user.getId())
                .orElse(BigDecimal.ZERO);
        BigDecimal userTotalCardFunds = this.userRepository.userCardSum(user.getId())
                .orElse(BigDecimal.ZERO);
        BigDecimal userTotalCredit = this.userRepository.findCreditsAmountById(user.getId())
                .orElse(BigDecimal.ZERO);

        return UserInfoDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userDebit(userTotalCashFunds.add(userTotalCardFunds))
                .userCredit(userTotalCredit)
                .build();
    }


    public List<UserForAdminPanelDTO> getAllUsersForAdminPanel() {
        return this.userRepository.findAll().stream()
                .map(u -> this.modelMapper.map(u, UserForAdminPanelDTO.class))
                .toList();
    }

    public UserProfileDTO provideUserProfileData(String username) {

        UserEntity entity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username for user profile"));

        return this.modelMapper.map(entity, UserProfileDTO.class);
    }

    private UpdatedUserProfileDTO provideUpdatedUserProfileData(String username) {

        UserEntity entity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username for user profile"));

        return this.modelMapper.map(entity, UpdatedUserProfileDTO.class);
    }


    public Authentication authenticateUser(String username, String password) {

        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));


    }
}
