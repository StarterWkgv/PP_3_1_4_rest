package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exception.UserValidationException;
import ru.kata.spring.boot_security.demo.mapper.UserDtoMapper;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminRestController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final UserDtoMapper userDtoMapper;

    public AdminRestController(UserService userService, UserValidator userValidator, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.userDtoMapper = userDtoMapper;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if (!userService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getById(id)
                .map(userDtoMapper::map)
                .orElseThrow(() -> new UserNotFoundException("User not found")), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll().stream()
                .map(userDtoMapper::map)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> editUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto user, BindingResult br) {
        userValidator.validate(user, br);
        if (br.hasErrors()) {
            throw new UserValidationException("user editing failed", br);
        }
        userService.update(user, id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addNewUser(@Valid @RequestBody UserDto user, BindingResult br) {
        userValidator.validate(user, br);
        if (br.hasErrors()) {
            throw new UserValidationException("couldn't add user", br);
        }
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}
