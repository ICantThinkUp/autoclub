package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/getProfile")
    private String getProfile() {
        return "Not implemented";
    }

    @GetMapping("/user/updateUsername")
    private String updateUsername() {
        return "Not implemented";
    }

    @DeleteMapping("/user/deleteProfile")
    private String deleteProfile() {
        return "Not implemented";
    }

}
