package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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
     * Метод поиска группы по токену.
     * @param token токен группы
     * @return найденная группа
     */
    public Group findByToken(String token){
        return groupRepository.findByToken(token).orElse(null);
    }

    /**
     * Метод поиска группы по Id админа группы.
     * @param adminId Id админа
     * @return найденная группа
     */
    public Group findByAdminId(Long adminId){
        return groupRepository.findByAdmin_Id(adminId).orElse(null);
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
     * Метод поиска группы по Id пользователя.
     * @param userId Id пользователя
     * @return группа
     */
    public Optional<Group> findByUserId(Long userId){
        return groupRepository.findByUserId(userId);
    }

    /**
     * Метод поиска группы по её названию.
     * @param groupName название группы
     * @return группа
     */
    public Group findByGroupName(String groupName){
        return groupRepository.findByGroupName(groupName).orElse(null);
    }
}
