package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model){
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role){
        userService.updateRole(id, role);
        return "redirect:/admin/users";
    }
}
