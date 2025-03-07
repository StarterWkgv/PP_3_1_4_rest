package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<User> findUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getById(id).orElseThrow(() -> new RuntimeException("User not found")), HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/admin/users/{id}")
    public ResponseEntity<HttpStatus> editUser(@PathVariable("id") Long id, @RequestBody User user) {
        checkUser(user);
        userService.update(user, id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<HttpStatus> addNewUser(@RequestBody User user) {
        checkUser(user);
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal User curUser) {
        return new ResponseEntity<>(userService.getById(curUser.getId()).orElseThrow(() -> new RuntimeException("User not found")), HttpStatus.OK);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(RuntimeException err) {
        Map<String, String> map = new HashMap<>();
        map.put("error", err.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    private void checkUser(User user) {
        if (user.getRoles().isEmpty() || user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty()) {
            throw new RuntimeException("Incorrect data format");
        }
    }

}
