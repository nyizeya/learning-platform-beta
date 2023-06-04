package com.jdc.admin.model.service.impl;

import com.jdc.admin.model.entity.Role;
import com.jdc.admin.model.entity.User;
import com.jdc.admin.model.repo.RoleRepo;
import com.jdc.admin.model.repo.UserRepo;
import com.jdc.admin.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public User loadUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User createUser(String email, String password) {
        User  user = new User(email, password);
        return userRepo.save(user);
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = userRepo.findByEmail(email);
        Role role = roleRepo.findByName(roleName);

        user.assignRoleToUser(role);
    }
}
