package com.example.moneyapp2.validation.matchingPasswordAndConfirmPassword;

import com.example.moneyapp2.model.dto.UserRegisterDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatcher implements ConstraintValidator<MatchingPasswordAndConfirmPassword, UserRegisterDTO> {

    @Override
    public void initialize(MatchingPasswordAndConfirmPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegisterDTO user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
