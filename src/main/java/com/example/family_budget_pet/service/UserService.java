package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.repository.GroupRepository;
import com.example.family_budget_pet.repository.RoleRepository;
import com.example.family_budget_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

//    @Transactional
//    public User save(User user, String mode){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.getRoles().addAll(getRoles(mode));
//        User u = userRepository.save(user);
//        return u;
//    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User updateRole(Long userId, String newRole){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(newRole).orElse(null);
        if (role != null && (newRole.equals("ROLE_READER") || newRole.equals("ROLE_USER")) ) {
            roles.add(role);
            user.setRoles(roles);
        }else if (role != null){
            roles.add(role);
            user.getRoles().addAll(roles);
        }
        return userRepository.save(user);
    }

    public Set<User> findByAdminGroupId(Long adminId){
        Group group = groupRepository.findByAdmin_Id(adminId).orElse(null);
        if (group == null) return null;
        Set<User> users = group.getUsers();
        return users;
    }

    @Transactional
    public User register(User user, String mode) {
        // Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Определяем роль
        String roleName = (mode.equals("admin"))
                ? "ROLE_ADMIN" : "ROLE_USER";

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(roleName).orElse(null);

        roles.add(role);
        user.getRoles().addAll(roles);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteGroup(Long adminId){
        User user = userRepository.findById(adminId).orElse(null);
        Group group = groupRepository.findByAdmin_Id(adminId).orElse(null);
        if (user != null && group != null){
            user.setGroups(new HashSet<>());
            groupRepository.delete(group);
        }
    }
}
