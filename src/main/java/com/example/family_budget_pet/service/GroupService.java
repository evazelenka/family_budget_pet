package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group save(Group group){
        String token = generateToken(group.getAdmin().getUsername(), group.getGroupName());
        group.setToken(token);
        return groupRepository.save(group);
    }

    public Group findByToken(String token){
        return groupRepository.findByToken(token).orElse(null);
    }

    private String generateToken(String rawAdminPart, String rawGroupPart){
        String[] adminPart = rawAdminPart.split("");
        String[] groupPart = rawGroupPart.split("");
        StringBuilder result = new StringBuilder();
        result.append("token");
        for (int i = 0; i < adminPart.length; i++) {
            Random rnd = new Random();
            result.append(adminPart[rnd.nextInt(adminPart.length)-1])
                    .append(groupPart[rnd.nextInt(groupPart.length-1)])
                    .append(rnd.nextInt(i));
        }
        return result.toString();
    }
}
