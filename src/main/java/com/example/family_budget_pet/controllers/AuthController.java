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

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model,
                            HttpSession httpSession){
        User user = userService.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)){
            model.addAttribute("title", "Вход");
            model.addAttribute("error", "Неверное имя пользователя или пароль!");
            return "auth/login";
        }

        httpSession.setAttribute("loggedUser", user);

        return "redirect:user/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
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
            return "register"; // возвращаем форму с сообщениями об ошибках
        }

        try {
            if ("admin".equals(mode)) {
                Group group = new Group();
                group.setGroupName(user.getUsername() + "'s Group");
                group.setAdmin(user);
                group.addUser(user);

                groupService.save(group);
                userService.save(user);

            } else if ("member".equals(mode) && groupToken.startsWith("token")) {
                Group group = groupService.findByToken(groupToken);
                if (group == null) {
                    model.addAttribute("error", "Группа с таким токеном не найдена!");
                    return "auth/register";
                }
                group.addUser(user);
                userService.save(user);
                groupService.save(group);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "auth/register";
        }

        return "redirect:/login";
    }
}


