package com.example.family_budget_pet.service;

import com.example.family_budget_pet.config.MyUserDetailsService;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
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
import org.springframework.stereotype.Service;

@Service
@Log
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final MyUserDetailsService userDetailsService;

    /**
     * Метод сохранения пользователя в БД.
     * @param user пользователь
     * @return сохраненный пользователь
     */
    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }

    /**
     * Метод поиска пользователя по имени.
     * @param username имя пользователя
     * @return найденный пользователь
     */
    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Метод изменения роли пользователя на противоположную(user<-->admin).
     * Используется для быстрого изменения роли без потери данных пользователя.
     * @param user пользователь
     * @throws RoleNotFoundException исключение, которое может возникнуть при запросе роли из БД
     */
    @Transactional
    public void changeRole(User user) throws RoleNotFoundException {
        Role newRole = new Role();
        if (user.getRole().getName().equals("ROLE_USER"))
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

    /**
     * Метод изменения роли пользователя на конкретную роль(user<-->reader).
     * Используется админом для выдачи прав суперпользователя(reader)
     * в группе.
     * @param userId id пользователя
     * @param newRole название новой роли
     * @return обновленный пользователь
     * @throws UserNotFoundException исключение, которое может возникнуть при запросе пользователя из БД
     * @throws RoleNotFoundException исключение, которое может возникнуть при запросе роли из БД
     */
    @Transactional
    public User updateRole(Long userId, String newRole) throws UserNotFoundException, RoleNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("пользователь " + userId + " не найден"));
        Role role = roleRepository.findByName(newRole).orElseThrow(() -> new RoleNotFoundException(newRole + " не найдена"));
        if (role != null){
            user.setRole(role);
        }
        userRepository.save(user);
        return user;
    }
}
