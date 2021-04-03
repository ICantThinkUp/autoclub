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

import java.util.ArrayList;
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

        // create admins (if down)
/* 1      roleRepository.deleteAll();
        roleRepository.save(new Role("ROLE_ADMIN"));
        roleRepository.save(new Role("ROLE_USER"));
 2

        Role roleAdmin = roleRepository.findRoleByName("ROLE_ADMIN");
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setRole(roleAdmin);
            userRepository.save(users.get(i));
        }
*/
        System.out.println("VERS 4");
        System.out.println("User repositry");
        System.out.println(userRepository.findAll());

        System.out.println("Roles");
        System.out.println(roleRepository.findAll());

    }
}
