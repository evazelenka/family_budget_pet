package com.example.family_budget_pet.service;

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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public User updateRole(Long userId, String newRole) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("пользователь " + userId + " не найден"));
        Role role = roleRepository.findByName(newRole).orElseThrow(() -> new RoleNotFoundException(newRole + " не найдена"));
        if (role != null){
            user.setRole(role);
        }
        userRepository.save(user);
        return user;
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
}
