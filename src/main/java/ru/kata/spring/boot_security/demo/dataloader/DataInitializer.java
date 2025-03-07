package ru.kata.spring.boot_security.demo.dataloader;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer {
    private final UserService userService;

    public DataInitializer(UserServiceImp userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        if (userService.getUserByEmail("admin").isEmpty()) {
            User admin = new User("Admin", "Admin", (byte) 30, "admin@mail.ru", new HashSet<>(List.of(roleAdmin, roleUser)), "admin");
            userService.save(admin);
        }
        if (userService.getUserByEmail("user").isEmpty()) {
            User user = new User("User", "User", (byte) 35, "user@mail.ru", new HashSet<>(List.of(roleUser)), "user");
            userService.save(user);
        }

    }
}