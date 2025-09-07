package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.TransactionService;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final GroupService groupService;
    private final TransactionService tService;

    @GetMapping("/transactions")
    public String showAllTransactions(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Set<User> users = userService.findByAdminGroupId(admin.getId());
        Set<Transaction> transactions = new HashSet<>();
        return "/admin/trans";
    }

    @GetMapping("/users")
    public String listUsers(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Long adminId = admin.getId();
        model.addAttribute("users", userService.findByAdminGroupId(adminId));
//        model.addAttribute("userRole", "user");
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role){
        userService.updateRole(id, role);
        return "redirect:/admin/users";
    }

    @GetMapping("/dashboard")
    public String adminPage(Model model){
        model.addAttribute("title", "Админка");
        return "general/dashboard.html";
    }

    @GetMapping("/group")
    public String groupPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Group group = groupService.findByAdminId(admin.getId());
        model.addAttribute("group", group);
        return "admin/group";
    }

    @PostMapping("/group/create")
    public String createNewGroup(@RequestParam String groupName, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Group group = groupService.save(groupName, admin);
        model.addAttribute("group", group);
        return "admin/group";
    }
}
