package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.mapper.UserDtoMapper;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder encoder;

    public UserServiceImp(UserRepository userRepository, UserDtoMapper userDtoMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.encoder = encoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .sorted((a, b) -> (int) (a.getId() - b.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void save(UserDto user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userDtoMapper.copy(user, new User()));
    }

    @Transactional
    @Override
    public boolean delete(long id) {
        return userRepository.delete(id);
    }

    @Transactional
    @Override
    public boolean update(UserDto dto, long id) {
        return getById(id).map(user -> {
            userRepository.update(userDtoMapper.copy(dto, user), id);
            return true;
        }).orElse(false);

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
