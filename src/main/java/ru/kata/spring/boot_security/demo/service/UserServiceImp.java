package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        roleRepository.saveAll(user.getRoles());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    @Override
    public void update(User user, long id) {
        getById(id).ifPresent(u -> {
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setAge(user.getAge());
            u.setEmail(user.getEmail());
            u.setRoles(user.getRoles());
            if (!user.getPassword().isEmpty()) {
                u.setPassword(encoder.encode(user.getPassword()));
            }
            userRepository.save(u);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
