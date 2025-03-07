package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    void save(User user);

    void delete(long id);

    void update(User user, long id);

    Optional<User> getById(long id);

    Optional<User> getUserByEmail(String email);

}
