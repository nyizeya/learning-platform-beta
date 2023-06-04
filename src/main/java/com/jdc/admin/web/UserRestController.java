package com.jdc.admin.web;

import com.jdc.admin.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public boolean checkIfEmailExists(@RequestParam(name = "email", defaultValue = "")  String email) {
        return userService.loadUserByEmail(email) != null;
    }

}
