package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.user.UserForAdminPanelDTO;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.dto.user.UserInfoDTO;
import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
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

    public UserForServicesDTO findUser(String username) {
        UserEntity entity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("User not found, based on principal username"));

        return this.modelMapper.map(entity, UserForServicesDTO.class);
    }

    public Long getTotalNumberOfAppUsers() {
        return this.userRepository.count();
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


}
