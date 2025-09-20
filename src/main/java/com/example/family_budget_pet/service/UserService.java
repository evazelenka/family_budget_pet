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

import java.util.List;
import java.util.Set;

@Service
@Log
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder; // например BCryptPasswordEncoder

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User updateRole(Long userId, String newRole){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Role role = roleRepository.findByName(newRole).orElse(null);
        if (role != null){
            user.setRole(role);
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
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException(roleName));
        System.out.println("____________________________________");
        System.out.println(role + " " + roleName);
        System.out.println("------------------------------------");
        user.setRole(role);
        return userRepository.save(user);
    }

//    @Transactional
//    public void deleteGroup(Long adminId){
//        User admin = userRepository.findById(adminId).orElse(null);
//        Group group = groupRepository.findByAdmin_Id(adminId).orElse(null);
//        if (admin != null && group != null){
//            admin.setGroup(null);
//            groupRepository.delete(group);
//        }
//    }

    @Transactional
    public void deleteGroup(Long adminId) {
        Group group = groupRepository.findByAdmin_Id(adminId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // сначала убрать связь у пользователей
        for (User user : group.getUsers()) {
            user.setGroup(null);
            userRepository.save(user);
        }

        // потом удалить саму группу
        groupRepository.delete(group);
    }

    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }
}
