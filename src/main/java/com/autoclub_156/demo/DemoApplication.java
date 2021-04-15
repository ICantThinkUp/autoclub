package com.autoclub_156.demo;

import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.interfaces.RoleRepository;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Role;
import com.autoclub_156.demo.model.User;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    CarRepository carRepository;

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        Logger logger = LoggerFactory.getLogger(DemoApplication.class);
        logger.info("Reload {}", DemoApplication.class.getName());

    }
}
