package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.RoleType;
import ru.kata.spring.boot_security.demo.util.UserEmailPasswordValidator;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final UserEmailPasswordValidator userEmailValidator;
    public UserController(UserService userService, UserEmailPasswordValidator userEmailPasswordValidator) {
        this.userService = userService;
        this.userEmailValidator = userEmailPasswordValidator;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("users", Collections.singleton(user));
        model.addAttribute("details", user);
        return "/user/user";
    }

    @GetMapping("/admin")
    public String showAllUsersPage(Model model, @AuthenticationPrincipal UserDetails ud) {
        List<User> list = userService.findAll();
        model.addAttribute("users", list);
        model.addAttribute("details", ud);
        model.addAttribute("rolList", RoleType.values());
        return "/admin/admin";
    }

    @GetMapping("/admin/new")
    public String showNewUserPage(Model model, @ModelAttribute("user") UserDto user,
                                  @AuthenticationPrincipal UserDetails ud) {
        model.addAttribute("details", ud);
        model.addAttribute("rolList", RoleType.values());
        return "/admin/new";
    }

    @PostMapping("/admin")
    public String addNewUser(Model model, @ModelAttribute("user") @Valid UserDto user, BindingResult br,
                             @AuthenticationPrincipal UserDetails ud) {
        model.addAttribute("details", ud);
        model.addAttribute("rolList", RoleType.values());
        userEmailValidator.validate(user, br);
        if (br.hasErrors()) {
            return "/admin/new";
        }
        userService.save(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/edit")
    public String showEditUserPage(@RequestParam("id") Long id, Model model) {
        User user = userService.getById(id).orElse(new User());
        model.addAttribute("user", user);
        return "/admin/edit";
    }

//    @PostMapping("/admin/edit")
//    public String editUser(@RequestParam("id") Long id,
//                           @ModelAttribute("user") @Valid User user, BindingResult br) {
//        if (br.hasErrors()) {
//            return "/admin/edit";
//        }
//        userService.update(user, id);
//        return "redirect:/admin";
//    }
}
