package ru.kata.spring.boot_security.demo.exception;

import org.springframework.validation.BindingResult;

public class UserValidationException extends RuntimeException {
    private final BindingResult bindingResult;

    public UserValidationException(String msg, BindingResult br) {
        super(msg);
        this.bindingResult = br;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
