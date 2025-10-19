package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.exceptions.RoleNotFoundException;
import com.example.family_budget_pet.repository.RoleRepository;
import com.example.family_budget_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder; // например BCryptPasswordEncoder
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Transactional
    public User register(User user, String mode) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String roleName = (mode.equals("admin"))
                ? "ROLE_ADMIN" : "ROLE_USER";
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(roleName + " не найдена"));
        user.setRole(role);
        return userRepository.save(user);
    }
}
