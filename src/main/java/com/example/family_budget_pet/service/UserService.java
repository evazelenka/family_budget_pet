package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.repository.GroupRepository;
import com.example.family_budget_pet.repository.RoleRepository;
import com.example.family_budget_pet.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder; // например BCryptPasswordEncoder

    public User save(User user, String mode){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Шифруем пароль");
        log.info("");
        user.getRoles().addAll(getRoles(mode));
        return userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User updateRole(Long userId, Role newRole){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(newRole);
        user.getRoles().addAll(roles);
        return userRepository.save(user);
    }

    public Set<User> findByAdminGroupId(Long adminId){
        Group group = groupRepository.findByAdmin_Id(adminId).orElse(null);
        if (group == null) return null;
        Set<User> users = group.getUsers();
        return users;
    }

//    @Transactional
//    public User register(User user) {
//        // Шифруем пароль
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // Определяем роль
//        String roleName = (user.getUsername().endsWith("8545") && user.getPassword().endsWith("8545"))
//                ? "ROLE_ADMIN" : "ROLE_USER";
//
//        Set<Role> roles = new HashSet<>();
//        Role role = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException(roleName + " not found"));
//        roles.add(role);
//        user.getRoles().addAll(roles);
//
//        return userRepository.save(user);
//    }

    private Set<Role> getRoles(String mode){
        Set<Role> roles = new HashSet<>();
        String roleName = (mode.equals("admin"))
                ? "ROLE_ADMIN" : "ROLE_USER";
        Role role = roleRepository.findByName(roleName).orElse(null);
        log.info(roleName + " роль пользователя");
        roles.add(role);
        return roles;
    }
}
