package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class UserController {
    private final RoleRepository roleRepository;

    public UserController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping({"/user", "/admin"})
    public String showUser(@AuthenticationPrincipal User curUser, Model model) {
        model.addAttribute("isAdmin", curUser.getRoles()
                .stream()
                .anyMatch(r -> r.getRole().equals("ADMIN")));
        model.addAttribute("rolList", roleRepository.findAll()
                .stream()
                .map(Role::getRole)
                .collect(Collectors.toList()));
        return "/page";
    }

}
