package com.example.family_budget_pet.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ROLE_USER')")
@AllArgsConstructor
public class UserController {

    @GetMapping("/dashboard")
    public String userPage(Model model){
        model.addAttribute("title", "Профиль");
        return "general/dashboard.html";
    }
}
