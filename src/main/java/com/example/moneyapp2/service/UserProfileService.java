package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.WrongPasswordException;
import com.example.moneyapp2.model.dto.user.UserForServicesDTO;
import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.model.dto.user.UserUpdatePasswordDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserService userService;

    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserProfileDTO updateUsername(String currentUsername, String newUsername) {

        UserForServicesDTO entity = this.userService.findUser(currentUsername);

        entity.setUsername(newUsername);

        this.userService.saveUserDtoToDB(entity);

        return provideUserProfileData(newUsername);
    }

    public UserProfileDTO provideUserProfileData(String username) {

        UserForServicesDTO entity = this.userService.findUser(username);

        return this.modelMapper.map(entity, UserProfileDTO.class);
    }

    public Authentication authenticateUser(String username, String password) {

        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
    }

    public void updateEmail(String username, String email) {

        UserForServicesDTO entity = this.userService.findUser(username);

        entity.setEmail(email);

        this.userService.saveUserDtoToDB(entity);
    }

    public void updatePassword(UserUpdatePasswordDTO user, String username) {

        String oldPassword = user.getOldPassword();
        String newPassword = user.getPassword();
        String confirmNewPassword = user.getConfirmPassword();

        UserForServicesDTO entity = this.userService.findUser(username);


        if (!passwordEncoder.matches(oldPassword, entity.getPassword()) ||
            !newPassword.equals(confirmNewPassword)) {

            throw new WrongPasswordException("Wrong password");
        }

        entity.setPassword(passwordEncoder.encode(newPassword));

        this.userService.saveUserDtoToDB(entity);
    }
}
