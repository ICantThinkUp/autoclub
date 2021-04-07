package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.*;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.CarService;
import com.autoclub_156.demo.services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class CarController {

    @Autowired
    UserService userService;

    @Autowired
    CarService carService;

    @PostMapping("/cars")
    public ResponseEntity addCar(HttpServletRequest request, @RequestBody AddCarRequest addCarRequest)
    {
        if (userService.isAdmin(request)) {
            Boolean isCarSaved = carService.saveCar(addCarRequest.vincode, addCarRequest.model,
                    addCarRequest.transmission);
            if (isCarSaved) {
                return ResponseEntity.status(201).build();
            }
            return ResponseEntity.status(208).build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/cars/{vincode}")
    public ResponseEntity getCarByVin(HttpServletRequest request, @PathVariable String vincode) {
        if (carService.isCarExist(vincode) && (
                userService.isTetheredCarToSender(request, vincode) || userService.isAdmin(request))) {
            return ResponseEntity.ok(carService.getCar(vincode));
        }
        return ResponseEntity.status(403).build();
    }

    /*
    * Этот метод стоит закинуть в UserController, чтобы клиентам
    * приходили персональные напоминания
    * (уточнить)
    * */
    @PutMapping("/cars/{vincode}/maintenance/?mode={value}")
    public ResponseEntity enableMaintenance(HttpServletRequest request, @RequestBody AddCarRequest addCarRequest, @PathVariable String value) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(addCarRequest.vincode)) {
            return ResponseEntity.status(404).build();
        }

        if (value.equals("1")) {
            carService.enableReminderAboutMaintenance(addCarRequest.vincode);
        } else {
            carService.disableReminderAboutMaintenance(addCarRequest.vincode);
        }

        return ResponseEntity.status(202).build();
    }

    @PutMapping("/cars/{vincode}/transmission")
    public ResponseEntity setTransmission(HttpServletRequest request, @PathVariable TransmissionChangeRequest transmissionChangeRequest) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(transmissionChangeRequest.vincode)) {
            return ResponseEntity.status(404).build();
        }

        carService.setTransmission(transmissionChangeRequest.vincode, transmissionChangeRequest.transmission);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cars/{vincode}/model")
    public ResponseEntity setModel(HttpServletRequest request, @PathVariable String vincode, @RequestBody ModelChangeRequest modelChangeRequest) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }

        carService.setModel(vincode, modelChangeRequest.model);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cars/{vincode}/vincode")
    public ResponseEntity setVincode(HttpServletRequest request, @PathVariable String vincode, @RequestBody VincodeChangeRequest vincodeChangeRequest) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }

        carService.setVincode(vincode, vincodeChangeRequest.vincode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cars/{vincode}")
    public ResponseEntity deleteCar(HttpServletRequest request, @PathVariable String vincode) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }

        carService.deleteCar(vincode);
        return ResponseEntity.ok().build();
    }
}
