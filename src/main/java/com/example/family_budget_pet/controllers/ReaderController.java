package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.service.ReaderService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reader")
@PreAuthorize("hasAuthority('ROLE_READER')")
@AllArgsConstructor
public class ReaderController {

    private final ReaderService service;


}
