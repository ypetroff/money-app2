package com.example.moneyapp2.validation.matchingPasswordAndConfirmPassword;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordsMatcher.class)
public @interface MatchingPasswordAndConfirmPassword {

    String message() default "Passwords don't match";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
