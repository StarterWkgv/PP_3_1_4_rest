package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping({"/", "/index"})
    public String index() {
        return "/index";
    }

    @GetMapping("/user")
    public String showUser(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails ud = (UserDetails) auth.getPrincipal();
        model.addAttribute("username",ud.getUsername());
        return "/user/user";
    }

    @GetMapping("/admin")
    public String showAllUsersPage(Model model) {
        List<User> list = userService.showAll();
        model.addAttribute("users", list);
        return "/admin/users";
    }

    @GetMapping("/admin/new")
    public String showNewUserPage(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping("/admin")
    public String addNewUser(@ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
            return "/admin/new";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String showEditUserPage(@RequestParam("id") Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "/admin/edit";
    }

    @PostMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
            return "/admin/edit";
        }
        userService.update(user, id);
        return "redirect:/admin";
    }
}
