package com.example.moneyapp2.service;

import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void saveUserToDB(UserEntity user) {
        this.userRepository.saveAndFlush(user);
    }

    public UserEntity getUser(String username) {
        return this.userRepository.findByUsername(username).orElseThrow();
    }
}
