package com.example.moneyapp2.test.config;

import com.example.moneyapp2.model.entity.user.MoneyAppUserDetails;
import com.example.moneyapp2.service.MoneyAppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestConfiguration
public class TestConfig {

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new MoneyAppUserDetailsService() {
//
//        };
//    }
}
