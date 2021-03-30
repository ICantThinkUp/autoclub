package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired UserService userService;

    @GetMapping("/users")
    private List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{login}")
    private User getUser(@PathVariable String login) {
        return userService.findByLogin(login);
    }

    @PutMapping("/user/{login}/name")
    private ResponseEntity updateUsername(HttpServletRequest request, @PathVariable String login, @RequestBody String newName) {
        String loginOfSender = userService.getLoginOfSender(request);
        if (!login.equals(loginOfSender)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            userService.editName(login, newName);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }

    }

    @PutMapping("/user/{login}/contactNumber")
    private ResponseEntity updateContactNumber(HttpServletRequest request, @PathVariable String login, @RequestBody String contactNumber) {
        if (userService.isSenderSameUser(request, login)) {
            userService.editContactNumber(login, contactNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/user/{login}/email")
    private ResponseEntity updateEmail(HttpServletRequest request, @PathVariable String login, @RequestBody String email) {
        if (userService.isSenderSameUser(request, login)) {
            userService.editEmail(login, email);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/{login}/cars")
    private ResponseEntity getCars(HttpServletRequest request, @PathVariable String login) {
        if (userService.isSenderSameUser(request, login) || userService.isAdmin(request)) {
            List<Car> cars = userService.getCarsByLogin(login);
            return ResponseEntity.ok().body(userService.getCarsByLogin(login));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/user/{login}/car/{vincode}")
    private ResponseEntity addCar(HttpServletRequest request, @PathVariable String login, String vincode) {
        if (userService.isAdmin(request)) {
            userService.addCar(login, vincode);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/user/{login}/car/{vincode}")
    private ResponseEntity deleteCar(HttpServletRequest request, @PathVariable String login, String vincode) {
        if (userService.isAdmin(request)) {
            userService.deleteCar(login, vincode);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/user/{login}")
    private ResponseEntity deleteUser(HttpServletRequest request, @PathVariable String login) {
        if (userService.isSenderSameUser(request, login) || userService.isAdmin(request)) {
            userService.deleteUser(login);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
