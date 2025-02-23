package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.kata.spring.boot_security.demo.util.RoleType;

@ControllerAdvice
public class ControllerAdviceImp {

    @ModelAttribute("rolList")
    public RoleType[] roleList() {
        return RoleType.values();
    }

    @ModelAttribute("currentUser")
    public UserDetails details(@AuthenticationPrincipal UserDetails ud) {
        return ud;
    }

    @ModelAttribute("testing")
    public String testing() {
        return "testingeeeeee";
    }
}
