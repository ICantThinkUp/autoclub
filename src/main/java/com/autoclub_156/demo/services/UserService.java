package com.autoclub_156.demo.services;

import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.interfaces.RoleRepository;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.Role;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.CustomUserDetails;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

@Service
@Log
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        Role userRole = roleRepository.findRoleByName("ROLE_USER");
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Boolean isLoginUsed(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Boolean bindCar(String vincode, String login) {
        User user = userRepository.findByLogin(login);
        Car car = carRepository.getCarByVincode(vincode);

        ArrayList<Car> cars = (user.getCars() == null) ? new ArrayList<Car>() : user.getCars();

        if (cars.contains(car)) {
            return false;
        }
        cars.add(car);

        user.setCars(cars);
        userRepository.save(user);
        return true;

    }

    public Boolean unbundCar(String vincode, String login) {
        User user = userRepository.findByLogin(login);
        for (int i = 0; i < user.getCars().size(); i++) {
            Car boundCar = user.getCars().get(i);

            System.out.println("boundCar.getVincode() == vincode");
            System.out.println(boundCar.getVincode() + " == " + vincode);

            if (boundCar.getVincode().equals(vincode)) {
                user.getCars().remove(boundCar);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Car> getCarsByLogin(String login) {
        return userRepository.findByLogin(login).getCars();
    }

    public Boolean isAccess(String login, String vincode) {
        ArrayList<Car> cars = new ArrayList<>();
        try {
            User user = userRepository.findByLogin(login);
            cars = user.getCars().isEmpty() ? cars : user.getCars();
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return cars.stream().anyMatch(x -> x.getVincode().equals(vincode)) || false;
    }
}
