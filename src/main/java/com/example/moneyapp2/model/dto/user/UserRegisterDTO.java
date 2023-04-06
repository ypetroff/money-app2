package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.validation.matchingPasswordAndConfirmPassword.MatchingPasswordAndConfirmPassword;
import com.example.moneyapp2.validation.uniqueEmail.UniqueEmail;
import com.example.moneyapp2.validation.uniqueUsername.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@MatchingPasswordAndConfirmPassword
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {

    @NotEmpty
    @Size(min = 3, message = "Password should be at least 3 characters long")
    private String password;

    @NotEmpty
    private String confirmPassword;

    @UniqueEmail
    @NotEmpty
    @Size(min = 5, message = "Email should be at least 5 characters long")
    @Email(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9_\\-\\.]{0,62}[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]{2,})+$",
            message = "Enter valid email address")
    private String email;

    @UniqueUsername
    @NotEmpty
    @Size(min = 4, max = 15, message = "Username should be between 4 and 15 characters long")
    private String username;

    @NotEmpty
    @Size(min = 3, max = 15, message = "First name should be between 4 and 15 characters long")
    private String firstName;

    @NotEmpty
    @Size(min = 3, max = 15, message = "Last name should be between 4 and 15 characters long")
    private String lastName;
}
