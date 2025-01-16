package ru.kata.spring.boot_security.demo.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        RoleType role = RoleType.valueOf(source);
        return new Role(role.ordinal()+1,role) ;
    }
}
