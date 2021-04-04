package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.AddCarRequest;
import com.autoclub_156.demo.controller.requests.BindCarRequest;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.CarService;
import com.autoclub_156.demo.services.UserService;
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
        if (userService.isTetheredCarToSender(request, vincode) || userService.isAdmin(request)) {
            return ResponseEntity.ok(carService.getCar(vincode));
        }
        return ResponseEntity.status(403).build();
    }

    /*
    * Этот метод стоит закинуть в UserController, чтобы клиентам
    * приходили персональные напоминания
    * (уточнить)
    * */
    @PutMapping("/cars/{vincode}/maintenance/?trigger={value}")
    public ResponseEntity enableMaintenance(HttpServletRequest request, @RequestBody AddCarRequest addCarRequest, @PathVariable String value) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (value.equals("1")) {
            carService.enableReminderAboutMaintenance(addCarRequest.vincode);
        } else {
            carService.disableReminderAboutMaintenance(addCarRequest.vincode);
        }

        return ResponseEntity.status(202).build();
    }

    @PutMapping("/cars/{vincode}/transmission")
    public ResponseEntity setCarTransmission(HttpServletRequest request, @PathVariable String vincode) {
        carService.setTransmission(vincode, transmissionRequest);
        // допилить с другими полями
    }

    @DeleteMapping("/cars/{vincode}")
    public Boolean deleteCar(@PathVariable String vincode) {
        return carService.deleteCar(vincode);
    }

}
