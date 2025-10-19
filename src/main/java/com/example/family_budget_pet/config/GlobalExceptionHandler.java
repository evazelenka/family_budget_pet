package com.example.family_budget_pet.config;

import com.example.family_budget_pet.exceptions.GroupNotFoundException;
import com.example.family_budget_pet.exceptions.RoleNotFoundException;
import lombok.extern.java.Log;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.logging.Level;

@ControllerAdvice
@Log
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleRoleNotFoundException(RoleNotFoundException ex, Model model){
        model.addAttribute("errorMessage", "Нам очень жаль, что возникла эта проблема. Мы уже работаем над её устранением. Пожалуйста, попробуйте перезагрузить страницу или повторить действие позже.");
        log.log(Level.WARNING, ex.getMessage() + " " + Arrays.toString(ex.getStackTrace()));
        return "error.html";
    }

    @ExceptionHandler
    public String handleGroupNotFoundException(GroupNotFoundException ex, Model model){
        model.addAttribute("errorMessage", "Пожалуйста, попробуйте перезагрузить страницу или повторить действие позже.");
        log.log(Level.WARNING, ex.getMessage() + " " + Arrays.toString(ex.getStackTrace()));
        return "error.html";
    }
}
