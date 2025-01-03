package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> showAll();

    void save(User user);

    void delete(long id);

    void update(User user, long id);

    User getById(long id);
}
