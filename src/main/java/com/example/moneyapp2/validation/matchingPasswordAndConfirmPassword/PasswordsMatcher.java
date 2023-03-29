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

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords don't match")
                    .addPropertyNode("confirmPassword").addConstraintViolation();
            return false;
        }

        return true;
    }
}
