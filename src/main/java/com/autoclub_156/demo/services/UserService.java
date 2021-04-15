package com.autoclub_156.demo.services;

import com.autoclub_156.demo.controller.CarController;
import com.autoclub_156.demo.controller.responses.UserResponse;
import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.interfaces.RoleRepository;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.Role;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public ArrayList<String> getCarsVincodesByLogin(String login) throws NullPointerException {
        ArrayList<String> carsVincodes = new ArrayList<String>();
        User user = userRepository.findByLogin(login);
        ArrayList<Car> cars = user.getCars();
        for (Car car : cars) {
            carsVincodes.add(car.getVincode());
        }
        return carsVincodes;
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
        user.setEmail(email);
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

    public Boolean addCar(String login, String vincode) {
        try {
            User user = userRepository.findByLogin(login);
            ArrayList<Car> cars = user.getCars();
            Car newCar = carRepository.getCarByVincode(vincode);
            cars.add(newCar);
            userRepository.save(user);
            return true;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public void deleteCar(String login, String vincode) {
        User user = userRepository.findByLogin(login);
        ArrayList<String> carsVincodesOfUser = getCarsVincodesByLogin(login);

        logger.info("Got car to delete " + vincode);
        logger.info("From " + carsVincodesOfUser);

        Boolean isCarTethered = isTheCarInUserList(carsVincodesOfUser, vincode);

        if (isCarTethered) {
            int indexOfDeletingCar = getIndexOfCar(carsVincodesOfUser, vincode);
            carsVincodesOfUser.remove(indexOfDeletingCar);
            userRepository.save(user);
        }
    }

    private int getIndexOfCar(ArrayList<String> cars, String car) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).equals(car)) {
                logger.info("Index of deleting car is " + i);
                return i;
            }
        }
        return -1;
    }

    private Boolean isTheCarInUserList(ArrayList<String> vincodes, String vincode) {
        logger.info("Check car in user list of car");
        for (int i = 0; i < vincodes.size(); i++) {
            if (vincodes.get(i).equals(vincode)) {
                return true;
            }
        }
        logger.info("Car dont found in user list");
        return false;
    }

    public void deleteUser(String login) {
        User user = userRepository.findByLogin(login);
        userRepository.delete(user);
    }

    public boolean isTetheredCarToSender(HttpServletRequest request, String vincode) {
        String loginOfSender = getLoginOfSender(request);
        try {
            ArrayList<String> vincodes = getCarsVincodesByLogin(loginOfSender);
            return isTheCarInUserList(vincodes, vincode);
        } catch (NullPointerException ex) {
            return false;
        }
    }
}
