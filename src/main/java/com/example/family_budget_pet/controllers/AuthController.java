package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user, Model model) {
        User savedUser = userService.register(user);
//        return ResponseEntity.ok(savedUser);
        return null;
    }
}
