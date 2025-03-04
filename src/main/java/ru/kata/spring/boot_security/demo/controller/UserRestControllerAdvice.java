package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exception.UserValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, String>> notValidExceptionHandler(UserValidationException err) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("isValidation", "true");
        err.getBindingResult()
                .getFieldErrors()
                .forEach(e -> errorMap.put(e.getField(), e.getDefaultMessage()));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundExceptionHandler(UserNotFoundException err) {
        return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
    }
}
