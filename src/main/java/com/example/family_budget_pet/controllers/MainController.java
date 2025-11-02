package com.example.family_budget_pet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kitty-cash")
public class MainController {

    @GetMapping("/welcome")
    public String welcome(Model model){
        model.addAttribute("title", "Главная");
        return "index2.html";
    }

    @GetMapping("/about")
    public String aboutProject(Model model){
        model.addAttribute("title", "О проекте");
        return "about.html";
    }
}
