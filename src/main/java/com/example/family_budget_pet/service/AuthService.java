package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final GroupService groupService;

    public AuthService(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

//    public boolean registerUser (User user, String groupToken){
//
//    }

    public boolean registerAdmin(User user){
        return true;
    }
}
