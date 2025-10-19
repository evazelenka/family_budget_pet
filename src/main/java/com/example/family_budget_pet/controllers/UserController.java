package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ROLE_USER')")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("/info")
    public String userPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User user = userService.findByUsername(principal.getUsername());
        model.addAttribute("title", "Профиль");
        model.addAttribute("user", user);
        return "general/info.html";
    }

    @PostMapping("/group/join")
    public String joinGroup(@RequestParam(required = false) String groupToken, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User user = userService.findByUsername(principal.getUsername());
        model.addAttribute("user", user);
        Group group;
        if (groupToken.startsWith("token")){
            group = groupService.findByToken(groupToken);
            if (group == null) {
                model.addAttribute("error", "Группа с таким токеном не найдена!");
                return "redirect:/user/info";
            }
            user.setGroup(group);
            userService.save(user);
            groupService.addUser(group, user);
        }
        return "redirect:/user/info";
    }

    @PostMapping("/group/leave")
    public String leaveGroup( Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User user = userService.findByUsername(principal.getUsername());
        Group group = user.getGroup();
        if (group == null) {
            model.addAttribute("error", "Группа не найдена!");
            return "redirect:/reader/users";
        }
        userService.leaveGroup(user, group);
        return "redirect:/reader/users";
    }

    @PostMapping("/change-role")
    public String changeMyRole(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User user = userService.findByUsername(principal.getUsername());
        if (user.getGroup() != null){
            model.addAttribute("error", "Пока вы состоите в группе, невозможно сменить роль.");
            return "redirect:/user/info";
        }
        userService.changeRole(user);
        return "redirect:/admin/info";
    }
}
