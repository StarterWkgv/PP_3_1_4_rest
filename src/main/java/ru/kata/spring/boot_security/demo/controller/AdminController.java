package ru.kata.spring.boot_security.demo.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

//@Controller
//@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserValidator userValidator;

    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String showAllUsersPage(Model model) {
        List<User> list = userService.findAll();
        model.addAttribute("users", list);
        return "/page";
    }

    @GetMapping("/new")
    public String showNewUserPage(@ModelAttribute("user") UserDto user) {
        return "/admin/new";
    }

    @PostMapping
    public String addNewUser(@ModelAttribute("user") @Valid UserDto user, BindingResult br) {
        userValidator.validate(user, br);
        if (br.hasErrors()) {
            return "/admin/new";
        }
        userService.save(user);
        return "redirect:/admin";
    }

}
