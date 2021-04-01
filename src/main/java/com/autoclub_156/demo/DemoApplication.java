package com.autoclub_156.demo;

import com.autoclub_156.demo.interfaces.RoleRepository;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Role;
import com.autoclub_156.demo.model.User;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        Role role_admin = roleRepository.findRoleByName("ROLE_ADMIN");
        List<User> users = userRepository.findAll();

        for (int i = 0; i < users.size(); i++) {
            users.get(i).setRole(role_admin);
            userRepository.save(users.get(i));
        }

        System.out.println("User repositry");
        System.out.println(userRepository.findAll());

        System.out.println("Roles");
        System.out.println(roleRepository.findAll());

    }
}
