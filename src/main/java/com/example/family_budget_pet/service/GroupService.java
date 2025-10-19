package com.example.family_budget_pet.service;

import com.example.family_budget_pet.config.MyUserDetailsService;
import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.exceptions.GroupNotFoundException;
import com.example.family_budget_pet.exceptions.RoleNotFoundException;
import com.example.family_budget_pet.repository.GroupRepository;
import com.example.family_budget_pet.repository.RoleRepository;
import com.example.family_budget_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MyUserDetailsService userDetailsService;

    /**
     * Метод добавления пользователя в группу.
     * @param group группа
     * @param user пользователь
     */
    @Transactional
    public void addUser(Group group, User user){
        group.addUser(user);
        groupRepository.save(group);
    }

    /**
     * Метод выхода из группы для пользователя и суперпользователя.
     * @param user пользователь
     * @param group группа
     */
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

    /**
     * Метод создания группы администратором.
     * @param groupName название группы
     * @param admin имя админа
     * @return сохраненная группа
     */
    @Transactional
    public Group save(String groupName, User admin){
        Group group = Group.builder()
                .groupName(groupName)
                .admin(admin)
                .token(generateToken(admin.getUsername(), groupName))
                .build();
        return groupRepository.save(group);
    }

    /**
     * Метод генерации токена для новой группы.
     * @param rawAdminPart данные(имя) админа
     * @param rawGroupPart данные(название) группы
     * @return уникальный токен группы
     */
    public String generateToken(String rawAdminPart, String rawGroupPart){
        String[] adminPart = rawAdminPart.split("");
        String[] groupPart = rawGroupPart.split("");
        StringBuilder result = new StringBuilder();
        result.append("token");
        for (int i = 0; i < adminPart.length; i++) {
            Random rnd = new Random();
            result.append(adminPart[rnd.nextInt(0, adminPart.length)])
                    .append(groupPart[rnd.nextInt(0, groupPart.length)])
                    .append(rnd.nextInt(i+1));
        }
        return result.toString();
    }

    /**
     * Метод удаления группы по id администратора.
     * @param adminId id администратора
     */
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

    /**
     * Метод поиска группы по токену.
     * @param token токен группы
     * @return найденная группа
     */
    public Group findByToken(String token){
        return groupRepository.findByToken(token).orElseThrow(() -> new GroupNotFoundException(token + " - токен группа не найдена"));
    }

    /**
     * Метод поиска группы по Id админа группы.
     * @param adminId Id админа
     * @return найденная группа
     */
    public Group findByAdminId(Long adminId){
        return groupRepository.findByAdmin_Id(adminId).orElseThrow(() -> new GroupNotFoundException(adminId + " - id админа группа не найдена"));
    }

    /**
     * Метод поиска группы по Id пользователя.
     * @param userId Id пользователя
     * @return группа
     */
    public Group findByUserId(Long userId){
        return groupRepository.findByUserId(userId).orElseThrow(() -> new GroupNotFoundException(userId + " - id пользователя группа не найдена"));
    }

    /**
     * Метод поиска группы по её названию.
     * @param groupName название группы
     * @return группа
     */
    public Group findByGroupName(String groupName){
        return groupRepository.findByGroupName(groupName).orElseThrow(() -> new GroupNotFoundException(groupName + " группа не найдена"));
    }
}
