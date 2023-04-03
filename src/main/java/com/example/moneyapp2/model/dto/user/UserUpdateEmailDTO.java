package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.validation.uniqueEmail.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateEmailDTO {

    @UniqueEmail
    @NotEmpty
    @Size(min = 5, message = "Email should be at least 5 characters long")
    @Email(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9_\\-\\.]{0,62}[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]{2,})+$",
            message = "Enter valid email address")
    private String email;

    @NotEmpty
    @Size(min = 3, message = "Password should be at least 3 characters long")
    private String password;
}
