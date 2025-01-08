package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
@Transactional()
public class UserServiceImp implements UserService
        , UserDetailsService {
    private final UserDao userDao;
    private final PasswordEncoder encoder;

    public UserServiceImp(UserDao userDao, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> showAll() {
        return userDao.showAll();
    }

    @Override
    public void save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public void delete(long id) {
        userDao.delete(id);
    }

    @Override
    public void update(User user, long id) {
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.update(user, id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(long id) {
        return userDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
