package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.stream.Collectors;

@Component
public class UserDtoMapper implements Mapper<User, UserDto> {
    private final RoleStringMapper roleStringMapper;

    public UserDtoMapper(RoleStringMapper roleStringMapper) {
        this.roleStringMapper = roleStringMapper;
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
}
