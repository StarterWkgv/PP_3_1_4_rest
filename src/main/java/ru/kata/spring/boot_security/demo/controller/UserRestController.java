package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.mapper.UserDtoMapper;
import ru.kata.spring.boot_security.demo.model.User;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final UserDtoMapper userDtoMapper;

    public UserRestController(UserDtoMapper userDtoMapper) {
        this.userDtoMapper = userDtoMapper;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(userDtoMapper.map(user),HttpStatus.OK);

    }


}
