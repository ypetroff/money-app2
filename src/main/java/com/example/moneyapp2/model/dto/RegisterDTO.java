package com.example.moneyapp2.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {
    private String username;
    private String password;

    private String email;
}
