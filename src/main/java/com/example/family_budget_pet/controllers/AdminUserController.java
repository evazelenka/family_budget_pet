package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("/users")
    public String listUsers(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User admin = userService.findByUsername(principal.getUsername());
        Long adminId = admin.getId();
        model.addAttribute("users", userService.findByAdminGroupId(adminId));
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
        String currentToken;
        String groupName;
        if (group == null){
            currentToken = "notoken";
            groupName = "nogroup";
        }
        else {
            currentToken = group.getToken();
            groupName = group.getGroupName();
        }
        model.addAttribute("groupToken", currentToken);
        model.addAttribute("groupName", groupName);
        return "admin/group";
    }

}
