package com.example.moneyapp2.exception;

public class UsernameOrPasswordDontMatchException extends RuntimeException {

    public UsernameOrPasswordDontMatchException(String message) {
        super(message);
    }
}
