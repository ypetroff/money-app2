package com.example.moneyapp2.web;

import com.example.moneyapp2.exception.ExpiredTokenException;
import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.exception.UsernameAlreadyTaken;
import com.example.moneyapp2.exception.UsernameOrPasswordDontMatchException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameOrPasswordDontMatchException.class)
    public Map<String, String> handleBadCredentialsException(UsernameOrPasswordDontMatchException ex) {

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Wrong credentials", ex.getMessage());

        return errorMap;
    }


    @ExceptionHandler({ExpiredJwtException.class, ExpiredTokenException.class} )
    @ResponseBody
    public ResponseEntity<?> handleExpiredTokenException(ExpiredTokenException ex) {

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("token_status", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyTaken.class)
    public Map<String, String> handleUsernameIsTaken(UsernameAlreadyTaken ex) {

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error message", ex.getMessage());

        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoAvailableDataException.class)
    public Map<String, String> handleMissingData(NoAvailableDataException ex) {

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error message", ex.getMessage());

        return errorMap;
    }



}
