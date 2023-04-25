package com.example.moneyapp2.web;

import com.example.moneyapp2.config.SecurityConfig;
import com.example.moneyapp2.model.dto.user.UserLoginDTO;
import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
import com.example.moneyapp2.service.MoneyAppUserDetailsService;
import com.example.moneyapp2.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUserWithCorrectInput() throws Exception {

        UserRegisterDTO user = UserRegisterDTO.builder()
                .username("test")
                .email("test@test.bg")
                .firstName("Test")
                .lastName("Testov")
                .password("12345")
                .confirmPassword("12345")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void registerUserWithIncorrectInput() throws Exception {

        UserRegisterDTO user = UserRegisterDTO.builder()
                .username("t")
                .email("test_test.bg")
                .firstName("Te")
                .lastName("")
                .password("123")
                .confirmPassword("345")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @WithUserDetails("test")
    void authenticateAndCreateToken() throws Exception {

        UserLoginDTO loginUser = UserLoginDTO.builder()
                .username("test")
                .password("12345")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}