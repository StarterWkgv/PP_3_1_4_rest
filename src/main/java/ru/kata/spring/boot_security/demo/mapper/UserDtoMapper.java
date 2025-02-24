package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.stream.Collectors;

@Component
public class UserDtoMapper implements Mapper<User, UserDto> {
    private final RoleStringMapper roleStringMapper;
    private final Converter<String, Role> converter;
    private final PasswordEncoder encoder;

    public UserDtoMapper(RoleStringMapper roleStringMapper, Converter<String, Role> converter, PasswordEncoder encoder) {
        this.roleStringMapper = roleStringMapper;
        this.converter = converter;
        this.encoder = encoder;
    }

    @Override
    public UserDto map(User obj) {
        return new UserDto(obj.getId(),
                obj.getFirstName(),
                obj.getLastName(),
                obj.getAge(),
                obj.getEmail(),
                obj.getRoles().stream()
                        .map(roleStringMapper::map)
                        .collect(Collectors.toSet()),
                "");
    }

    @Override
    public User copy(UserDto from, User to) {
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setAge((byte) from.getAge());
        to.setEmail(from.getEmail());
        to.setRoles(from.getRoles().stream()
                .map(converter::convert)
                .collect(Collectors.toSet()));
        if (!from.getPassword().isBlank()) {
            to.setPassword(encoder.encode(from.getPassword()));
        }
        return to;
    }
}
