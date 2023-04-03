package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.validation.uniqueUsername.UniqueUsername;
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
public class UsernameUpdateDTO {

    @UniqueUsername
    @NotEmpty
    @Size(min = 4, max = 15, message = "Username should be between 4 and 15 characters long")
    private String username;

    @NotEmpty
    @Size(min = 3, message = "Password should be at least 3 characters long")
    private String password;
}
