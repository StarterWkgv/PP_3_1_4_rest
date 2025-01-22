package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService
        , UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public List<User> showAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Transactional
    @Override
    public void update(User user, long id) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.update(user, id);
    }

    @Override
    public Optional<User> getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
