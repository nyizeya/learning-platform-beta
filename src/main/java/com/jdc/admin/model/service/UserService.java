package com.jdc.admin.model.service;

import com.jdc.admin.model.entity.User;

public interface UserService {

    User loadUserByEmail(String email);

    User createUser(String email, String password);

    void assignRoleToUser(String email, String roleName);

}
