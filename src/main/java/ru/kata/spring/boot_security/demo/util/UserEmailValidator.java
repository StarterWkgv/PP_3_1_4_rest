package ru.kata.spring.boot_security.demo.util;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;


@Component
public class UserEmailValidator implements Validator {
    private final UserService userService;

    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Optional<User> o = userService.getUserByEmail(((UserDto) target).getEmail());
        if (o.isPresent() && ((UserDto) target).getId() != o.get().getId() ) {
            errors.rejectValue("email", "", "The user with this email already exists");
        }


    }
}
