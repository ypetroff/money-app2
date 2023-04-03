package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.model.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserService userService;

    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    public UserProfileDTO updateUsername(String currentUsername, String newUsername) {

        UserEntity entity = this.userService.findUser(currentUsername);

        entity.setUsername(newUsername);

        this.userService.saveUserToDB(entity);

        return provideUserProfileData(newUsername);
    }

    public UserProfileDTO provideUserProfileData(String username) {

        UserEntity entity = this.userService.findUser(username);

        return this.modelMapper.map(entity, UserProfileDTO.class);
    }

    public Authentication authenticateUser(String username, String password) {

        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));


    }

    public void updateEmail(String username, String email) {

        UserEntity entity = this.userService.findUser(username);

        entity.setEmail(email);

        this.userService.saveUserToDB(entity);
    }
}
