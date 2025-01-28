package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    void save(UserDto user);

    boolean delete(long id);

    boolean update(UserDto user, long id);

    Optional<User> getById(long id);

    Optional<User> getUserByEmail(String email);

}
