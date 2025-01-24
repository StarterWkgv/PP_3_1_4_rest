package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.exception.UserValidationException;
import ru.kata.spring.boot_security.demo.mapper.UserDtoMapper;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserEmailValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AdminRestController {
    private final UserService userService;
    private final UserEmailValidator userEmailValidator;
    private final UserDtoMapper userDtoMapper;

    public AdminRestController(UserService userService, UserEmailValidator userEmailValidator, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userEmailValidator = userEmailValidator;
        this.userDtoMapper = userDtoMapper;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if (!userService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        ;
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.showAll().stream().map(userDtoMapper::map)
                .collect(Collectors.toList()), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto user, BindingResult br) {

        userEmailValidator.validate(user,br);
        if (br.hasErrors()){
            throw new UserValidationException("user editing failed", br );
        }
        userService.update(user, id);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, String>> notValidExceptionHandler(UserValidationException err) {
        Map<String, String> errorMap = new HashMap<>();
        err.getBindingResult()
                .getFieldErrors()
                .forEach(e -> {
                    errorMap.put(e.getField(), e.getDefaultMessage());
                });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
