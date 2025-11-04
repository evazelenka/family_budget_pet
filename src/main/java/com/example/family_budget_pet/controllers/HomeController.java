package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.exceptions.RoleNotFoundException;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kitty-cash")
@AllArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome(Model model){
        model.addAttribute("title", "Главная");
        return "index2.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String getProfilePage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User user = userService.findByUsername(principal.getUsername());
        String roleName = user.getRole().getName();
        if (roleName.equals("ROLE_USER")) return "redirect:/user/profile";
        if (roleName.equals("ROLE_ADMIN")) return "redirect:/admin/profile";
        if (roleName.equals("ROLE_READER")) return "redirect:/reader/profile";
        else throw new RoleNotFoundException("Нераспознана роль пользователя " + roleName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER'")
    @GetMapping("/group")
    public String getGroupPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User user = userService.findByUsername(principal.getUsername());
        String roleName = user.getRole().getName();
        if (roleName.equals("ROLE_ADMIN")) return "redirect:/admin/group";
        if (roleName.equals("ROLE_READER")) return "redirect:/reader/group";
        else throw new RoleNotFoundException("Нераспознана роль пользователя " + roleName);
    }
}
