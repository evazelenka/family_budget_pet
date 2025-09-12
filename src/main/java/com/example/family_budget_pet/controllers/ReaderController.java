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

import java.util.List;

@Controller
@RequestMapping("/reader")
@PreAuthorize("hasAuthority('ROLE_READER')")
@AllArgsConstructor
public class ReaderController {

    private final ReaderService service;
    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("/users")
    public String listUsers(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User reader = userService.findByUsername(principal.getUsername());
        List<Group> groups = groupService.findByUserId(reader.getId());

        if (groups != null && !groups.isEmpty()){
            model.addAttribute("users", groups.get(0).getUsers());
            model.addAttribute("group", groups.get(0));
        }
        model.addAttribute("title", "Группа");
        return "reader/users";
    }

    @PostMapping("/group/join")
    public String joinGroup(@RequestParam(required = false) String groupToken, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User reader = userService.findByUsername(principal.getUsername());
        Group group;
        if (groupToken.startsWith("token")){
           group = groupService.findByToken(groupToken);
            if (group == null) {
                model.addAttribute("error", "Группа с таким токеном не найдена!");
                return "reader/users";
            }
            groupService.addUser(group, reader);
            model.addAttribute("users", group.getUsers());
            model.addAttribute("group", group);
        }
        model.addAttribute("title", "Группа");
        return "reader/users";
    }
}
