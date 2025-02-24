package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;

@Component
public class RoleStringMapper implements Mapper<Role, String> {
    @Override
    public String map(Role obj) {
        return obj.getAuthority();
    }
}
