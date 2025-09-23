package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.ReaderService;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Set;

@Controller
@RequestMapping("/reader")
@PreAuthorize("hasAuthority('ROLE_READER')")
@AllArgsConstructor
public class ReaderController {

    private final ReaderService service;
    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("/info")
    public String readerPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User reader = userService.findByUsername(principal.getUsername());
        model.addAttribute("title", "Профиль");
        model.addAttribute("user", reader);
        return "general/info.html";
    }

    @GetMapping("/users")
    public String listUsers(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User reader = userService.findByUsername(principal.getUsername());
        Group group = groupService.findByUserId(reader.getId()).orElse(null);
        if (group != null){
            Set<User> users = group.getUsers();
            model.addAttribute("users", users);
            model.addAttribute("users", group.getUsers());
            model.addAttribute("group", group);
        }
        model.addAttribute("title", "Группа");
        return "admin-reader/users";
    }

    @PostMapping("/group/join")
    public String joinGroup(@RequestParam(required = false) String groupToken, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User reader = userService.findByUsername(principal.getUsername());
        Group group;
        if (groupToken.startsWith("token")){
           group = groupService.findByToken(groupToken);
            if (group == null) {
                model.addAttribute("error", "Группа с таким токеном не найдена!");
                return "redirect:/reader/users";
            }
            reader.setGroup(group);
            userService.save(reader);
            groupService.addUser(group, reader);
        }
        return "redirect:/reader/users";
    }
}
