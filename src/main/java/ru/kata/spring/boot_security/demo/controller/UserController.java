package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index() {
        return "redirect:/users";
    }

    @GetMapping("/user")
    public String showUser(){
        return "/users/user";
    }

    @GetMapping("/users")
    public String showAllUsersPage(Model model) {
        List<User> list = userService.showAll();
        model.addAttribute("users", list);
        return "/users/users";
    }

    @GetMapping("/users/new")
    public String showNewUserPage(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @PostMapping("/users")
    public String addNewUser(@ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
            return "/users/new";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/users/edit")
    public String showEditUserPage(@RequestParam("id") Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/users/edit")
    public String editUser(@RequestParam("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
            return "/users/edit";
        }
        userService.update(user, id);
        return "redirect:/users";
    }
}
