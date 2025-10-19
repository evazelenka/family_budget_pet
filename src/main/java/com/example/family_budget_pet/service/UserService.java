package com.example.family_budget_pet.service;

import com.example.family_budget_pet.config.MyUserDetailsService;
import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.exceptions.GroupNotFoundException;
import com.example.family_budget_pet.exceptions.RoleNotFoundException;
import com.example.family_budget_pet.exceptions.UserNotFoundException;
import com.example.family_budget_pet.repository.GroupRepository;
import com.example.family_budget_pet.repository.RoleRepository;
import com.example.family_budget_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final MyUserDetailsService userDetailsService;

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User updateRole(Long userId, String newRole){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("пользователь " + userId + " не найден"));
        Role role = roleRepository.findByName(newRole).orElseThrow(() -> new RoleNotFoundException(newRole + " не найдена"));
        if (role != null){
            user.setRole(role);
        }
        userRepository.save(user);
        return user;
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
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(roleName + " не найдена"));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteGroup(Long adminId) {
        Group group = groupRepository.findByAdmin_Id(adminId)
                .orElseThrow(() -> new GroupNotFoundException("группа админа " + adminId + " не найдена"));

        for (User user : group.getUsers()) {
            user.setGroup(null);
            userRepository.save(user);
        }

        groupRepository.delete(group);
    }

    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }

    @Transactional
    public void leaveGroup(User user, Group group) {
        user.setGroup(null);
        group.getUsers().remove(user);
        if (user.getRole().getName().equals("ROLE_READER")){
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RoleNotFoundException("ROLE_USER не найдена"));
            user.setRole(role);
        }
        userRepository.save(user);
        groupRepository.save(group);

        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(user.getUsername());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                updatedUserDetails.getPassword(),
                updatedUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional
    public void changeRole(User user) throws RoleNotFoundException {
        Role newRole = new Role();
        if (user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_READER"))
            newRole = roleRepository.findByName("ROLE_ADMIN").
                    orElseThrow(() -> new RoleNotFoundException("ROLE_ADMIN не найдена"));

        else newRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER не найдена"));

        user.setRole(newRole);
        userRepository.save(user);

        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(user.getUsername());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                updatedUserDetails.getPassword(),
                updatedUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
