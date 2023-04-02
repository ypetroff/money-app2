package com.example.moneyapp2.exception;

public class UsernameAlreadyTaken extends RuntimeException{

    public UsernameAlreadyTaken(String message) {
        super(message);
    }
}
