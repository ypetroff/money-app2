package com.example.moneyapp2.model.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordDTO {

    @NotEmpty(message = "Old password field cannot be empty String or null")
    private String oldPassword;

    @NotEmpty
    @Size(min = 3, message = "Password should be at least 3 characters long")
    private String password;

    @NotEmpty
    private String confirmPassword;
}
