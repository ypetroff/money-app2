package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
import com.example.moneyapp2.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
             //todo   .andExpect(ResponseEntity.)
//        {
//            "lastName": "Last name should be between 3 and 15 characters long",
//                "firstName": "First name should be between 3 and 15 characters long",
//                "password": "Password should be at least 3 characters long",
//                "confirmPassword": "Passwords don't match",
//                "email": "Enter valid email address",
//                "username": "Username should be between 3 and 23 characters long"
//        }
    }

    @Test
    void authenticateAndCreateToken() {
    }
}