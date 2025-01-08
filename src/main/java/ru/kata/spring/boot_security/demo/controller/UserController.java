package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.RoleType;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUser(@AuthenticationPrincipal User user, Model model) {
        Set<String> roles = AuthorityUtils.authorityListToSet(user.getAuthorities());
        model.addAttribute("users", Collections.singleton(user));
        model.addAttribute("details", user);
        return "/user/info";
    }

    @GetMapping("/admin")
    public String showAllUsersPage(Model model, @AuthenticationPrincipal UserDetails ud) {
        List<User> list = userService.showAll();
        Set<String> roles = AuthorityUtils.authorityListToSet(ud.getAuthorities());
        model.addAttribute("users", list);
        model.addAttribute("details", ud);
//        model.addAttribute("adminPanel", true);
//        model.addAttribute("userName", ud.getUsername());
//        model.addAttribute("roles", roles);

        return "/user/info";
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
