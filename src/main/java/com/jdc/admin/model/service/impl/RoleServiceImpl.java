package com.jdc.admin.model.service.impl;

import com.jdc.admin.model.entity.Role;
import com.jdc.admin.model.repo.RoleRepo;
import com.jdc.admin.model.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public Role createRole(String roleName) {
        return roleRepo.save(new Role(roleName));
    }
}
