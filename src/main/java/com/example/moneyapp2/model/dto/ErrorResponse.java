package com.example.moneyapp2.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private String type;
    private List<String> errors;

    public ErrorResponse(String type, List<String> errors) {
        this.type = type;
        this.errors = errors;
    }

    // Add setters if necessary
}

