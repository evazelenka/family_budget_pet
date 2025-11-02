package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.*;
import com.example.family_budget_pet.exceptions.UserNotFoundException;
import com.example.family_budget_pet.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final GroupService groupService;
    private final TransactionService tService;

    @GetMapping("/users")
    public String listUsers(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Long adminId = admin.getId();
        Group group = groupService.findByAdminId(adminId);
        if (group != null){
            Set<User> users = group.getUsers();
            users.stream().sorted(Comparator.comparing(User::getId));
            model.addAttribute("users", users);
            model.addAttribute("group", group);
        }
        model.addAttribute("title", "Группа");
        return "admin-reader/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam String role, Model model){
        try {
            userService.updateRole(id, role);
        } catch (UserNotFoundException e) {
            model.addAttribute("error", "Что-то пошло не так... Обновите страницу.");
            return "redirect:/admin/users";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/info")
    public String adminPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        model.addAttribute("title", "Админка");
        model.addAttribute("user", admin);
        return "general/info.html";
    }

    @PostMapping("/group/create")
    public String createNewGroup(@RequestParam String groupName, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Group group = groupService.findByGroupName(groupName);
        if (group != null){
            model.addAttribute("error", "Группа с таким именем уже существует!");
            return "redirect:/admin/users";
        }
        group = groupService.save(groupName, admin);
        admin.setGroup(group);
        userService.save(admin);
        model.addAttribute("group", group);
        return "redirect:/admin/users";
    }


    @PostMapping("/group/delete")
    public String deleteGroup(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        groupService.deleteGroup(admin.getId());
        return "redirect:/admin/users";
    }
}
