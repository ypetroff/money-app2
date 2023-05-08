package com.example.moneyapp2.exception;

public class NoSuchCategoryException extends IllegalArgumentException{

    public NoSuchCategoryException(String message) {
        super(message);
    }
}
