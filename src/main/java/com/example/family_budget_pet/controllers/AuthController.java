package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private  final GroupService groupService;

    public AuthController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        model.addAttribute("title", "Вход");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Регистрация");
        return "auth/register"; // имя Thymeleaf шаблона
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam String mode,
            @RequestParam(required = false) String groupToken,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register"; // возвращаем форму с сообщениями об ошибках
        }

        try {
            if ("admin".equals(mode)) {
                userService.register(user, mode);
            } else if ("member".equals(mode) && groupToken.startsWith("token")) {
                Group group = groupService.findByToken(groupToken);
                if (group == null) {
                    model.addAttribute("error", "Группа с таким токеном не найдена!");
                    return "auth/register";
                }
                userService.register(user, mode);
                groupService.addUser(group, user);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "auth/register";
        }

        return "redirect:auth/login";
    }
}


