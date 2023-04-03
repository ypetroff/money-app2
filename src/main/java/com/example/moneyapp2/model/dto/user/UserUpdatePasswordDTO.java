package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.validation.matchingPasswordAndConfirmPassword.MatchingPasswordAndConfirmPassword;
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
public class UserUpdatePasswordDTO {

    private String oldPassword;

    @NotEmpty
    @Size(min = 3, message = "Password should be at least 3 characters long")
    private String password;

    @NotEmpty
    private String confirmPassword;
}
