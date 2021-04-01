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

        System.out.println("VERSION 3");

        roleRepository.deleteAll();

        roleRepository.save(new Role("ROLE_ADMIN"));

        roleRepository.save(new Role("ROLE_USER"));

        System.out.println("Roles");
        System.out.println(roleRepository.findAll());

    }
}
