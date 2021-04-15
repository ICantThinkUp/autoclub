package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.ChangePasswordRequest;
import com.autoclub_156.demo.controller.requests.SearchRequest;
import com.autoclub_156.demo.controller.responses.BindCarToUserResponse;
import com.autoclub_156.demo.controller.responses.UpdateUsernameResponse;
import com.autoclub_156.demo.controller.responses.UserResponse;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.CarService;
import com.autoclub_156.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired CarService carService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    private ResponseEntity<List<UserResponse>> getUsers(@RequestParam SearchRequest searchRequest) {
        int amount = searchRequest.amount;
        int offset = searchRequest.offset;

        ArrayList<UserResponse> users = userService.getAllUsers();

        // IndexOutOfBoundsException

        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/user/{login}")
    private ResponseEntity<UserResponse> getUser(@PathVariable String login) {
        System.out.println("get the user run");

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
            List<String> vincodesOfUserCars = userService.getCarsVincodesByLogin(login);
            // добавить оффсет с количеством выдаваемых машин, принадлежащих одному юзерру
            return ResponseEntity.ok().body(vincodesOfUserCars);
        }
        return ResponseEntity.status(403).build();
    }

    // привязка существубщей машины к пользователю
    @PutMapping("/user/{login}/car/{vincode}")
    private ResponseEntity addCar(HttpServletRequest request, @PathVariable String login, @PathVariable String vincode) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }
        userService.addCar(login, vincode);
        return ResponseEntity.status(200).build();
    }

    // отвязка машины от пользователя
    @DeleteMapping("/user/{login}/car/{vincode}")
    private ResponseEntity deleteCar(HttpServletRequest request, @PathVariable String login, @PathVariable String vincode) {
        if (carService.isCarExist(vincode) && userService.isAdmin(request)) {
            userService.deleteCar(login, vincode);
            return ResponseEntity.ok().build();
        } else if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(403).build();
        }
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
