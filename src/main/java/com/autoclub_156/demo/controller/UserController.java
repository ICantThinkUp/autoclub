package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.ChangePasswordRequest;
import com.autoclub_156.demo.controller.responses.BindCarToUserResponse;
import com.autoclub_156.demo.controller.responses.UpdateUsernameResponse;
import com.autoclub_156.demo.controller.responses.UserResponse;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired UserService userService;

    @GetMapping("/users")
    private ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @GetMapping("/user/{login}")
    private ResponseEntity<UserResponse> getUser(@PathVariable String login) {
        User user = userService.findByLogin(login);
        return ResponseEntity.status(200).body(new UserResponse(user.getLogin(),
                user.getName(), user.getEmail(), user.getEmail()));
    }

    @PutMapping("/user/{login}/name")
    private ResponseEntity updateUsername(HttpServletRequest request, @PathVariable String login, @RequestBody UpdateUsernameResponse updateUsernameResponse) {
        if (!userService.isSenderSameUser(request, login)) {
            return ResponseEntity.status(403).build();
        }
        try {
            userService.editName(login, updateUsernameResponse.name);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }

    }

    @PutMapping("/user/{login}/password")
    private ResponseEntity changePassword(HttpServletRequest request, @PathVariable String login, @RequestBody ChangePasswordRequest passwords) {
        if (!userService.isSenderSameUser(request, login)) {
            ResponseEntity.status(403).build();
        }
        System.out.println("Try to change password...");
        Boolean isChangedPassword = userService.changePassword(login, passwords.oldPassword, passwords.newPassword);
        if (isChangedPassword) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/user/{login}/contactNumber")
    private ResponseEntity updateContactNumber(HttpServletRequest request, @PathVariable String login, @RequestBody UserResponse userResponse) {
        if (userService.isSenderSameUser(request, login)) {
            userService.editContactNumber(login, userResponse.contactNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/user/{login}/email")
    private ResponseEntity updateEmail(HttpServletRequest request, @PathVariable String login, @RequestBody UserResponse userResponse) {
        if (userService.isSenderSameUser(request, login)) {
            userService.editEmail(login, userResponse.email);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/user/{login}/cars")
    private ResponseEntity getCars(HttpServletRequest request, @PathVariable String login) {
        if (userService.isSenderSameUser(request, login) || userService.isAdmin(request)) {
            List<Car> cars = userService.getCarsByLogin(login);
            return ResponseEntity.ok().body(userService.getCarsByLogin(login));
        }
        return ResponseEntity.status(403).build();
    }

    // привязка существубщей машины к пользователю
    @PutMapping("/user/{login}/car/{vincode}")
    private ResponseEntity addCar(HttpServletRequest request, @PathVariable BindCarToUserResponse bindCarToUserResponse) {
        if (userService.isAdmin(request)) {
            userService.addCar(bindCarToUserResponse.login, bindCarToUserResponse.vincode);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    // отвязка машины от пользователя
    @DeleteMapping("/user/{login}/car/{vincode}")
    private ResponseEntity deleteCar(HttpServletRequest request, @PathVariable BindCarToUserResponse bindCarToUserResponse) {
        if (userService.isAdmin(request)) {
            userService.deleteCar(bindCarToUserResponse.login, bindCarToUserResponse.vincode);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @DeleteMapping("/user/{login}")
    private ResponseEntity deleteUser(HttpServletRequest request, @PathVariable String login) {
        if (userService.isSenderSameUser(request, login) || userService.isAdmin(request)) {
            userService.deleteUser(login);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }
}
