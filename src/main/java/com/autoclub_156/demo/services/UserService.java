package com.autoclub_156.demo.services;

import com.autoclub_156.demo.controller.responses.UserResponse;
import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.interfaces.RoleRepository;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.Role;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.CustomUserDetails;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public User saveUser(String login, String password) {
        User user = new User();
        Role userRole = roleRepository.findRoleByName("ROLE_USER");
        user.setRole(userRole);
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Boolean changePassword(String login, String oldPassword, String newPassword) {
        User user = findByLoginAndPassword(login, oldPassword);
        System.out.println("login");
        System.out.println(login);
        System.out.println("oldPassword");
        System.out.println(oldPassword);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Boolean isLoginUsed(String login) {
        System.out.println(userRepository.findByLogin(login));
        return userRepository.findByLogin(login) != null;
    }

    public User findByLogin(String login) {
        User user = userRepository.findByLogin(login);
        return user;
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

    public List<UserResponse> getAllUsers() {
        List<User> rawUsers = userRepository.findAll();
        List<UserResponse> preparedUsers = new ArrayList<UserResponse>();

        for (int i = 0; i < rawUsers.size(); i++) {
            User currentUser = rawUsers.get(i);
            preparedUsers.add(prepareUserToSend(currentUser));
        }
        return preparedUsers;
    }

    public UserResponse prepareUserToSend(User user) {
        UserResponse preparedUser = new UserResponse(user.getLogin(), user.getName(),
                user.getContactNumber(), user.getEmail());
        return preparedUser;
    }

    public String getLoginOfSender(HttpServletRequest request) throws NullPointerException {
        Authentication auth = (Authentication) request.getUserPrincipal();

        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

        return customUserDetails.getUsername();
    }

    public void editName(String login, String newName) {
        User user = findByLogin(login);
        user.setName(newName);
        userRepository.save(user);
    }

    public boolean isSenderSameUser(HttpServletRequest request, String login) {
        String loginOfSender;
        try {
            loginOfSender = getLoginOfSender(request);
            if (!login.equals(loginOfSender)) {
                return false;
            }
            return true;
        } catch (NullPointerException ex) {
            System.out.println("request");
            System.out.println(request);
            return false;
        }
    }

    public void editContactNumber(String login, String contactNumber) {
        User user = findByLogin(login);
        user.setContactNumber(contactNumber);
        userRepository.save(user);
    }

    public void editEmail(String login, String email) {
        User user = findByLogin(login);
        user.setContactNumber(email);
        userRepository.save(user);
    }

    public boolean isAdmin(HttpServletRequest request) {
        Authentication auth = (Authentication) request.getUserPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        String userRole = customUserDetails.getRole().getName();
        if (userRole.equalsIgnoreCase("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }

    public void addCar(String login, String vincode) {
        User user = userRepository.findByLogin(login);
        ArrayList<Car> cars = user.getCars();
        Car newCar = carRepository.getCarByVincode(vincode);
        cars.add(newCar);
        userRepository.save(user);
    }

    public void deleteCar(String login, String vincode) {
        User user = userRepository.findByLogin(login);
        ArrayList<Car> cars = user.getCars();
        Car deletingCar = carRepository.getCarByVincode(vincode);
        cars.add(deletingCar);
        userRepository.save(user);
    }

    public void deleteUser(String login) {
        User user = userRepository.findByLogin(login);
        userRepository.delete(user);
    }
}
