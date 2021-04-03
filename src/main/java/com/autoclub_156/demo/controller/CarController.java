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

    @PostMapping("/admin/cars")
    public ResponseEntity addCar(@RequestBody AddCarRequest addCarRequest)
    {
        Boolean isCarCreated = carService.saveCar(addCarRequest.vincode, addCarRequest.model,
                addCarRequest.transmission);
        if (isCarCreated) {
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/user/car/{vincode}")
    public ResponseEntity getCarByVin(HttpServletRequest request, @PathVariable String vincode) {
        if (userService.isTetheredCarToSender(request, vincode) || userService.isAdmin(request)) {
            return ResponseEntity.ok(carService.getCar(vincode));
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/admin/enableMaintenance")
    public Boolean enableMaintenance(@RequestBody AddCarRequest request) {
        return carService.enableReminderAboutMaintenance(request.vincode);
    }

    @PostMapping("/admin/disableMaintenance")
    public Boolean disableMaintenance(@RequestBody AddCarRequest request) {
        return carService.disableReminderAboutMaintenance(request.vincode);
    }

    @PostMapping("/admin/updateCar")
    public String updateCar() {
        return "Not implemented";
    }

    @DeleteMapping("/admin/deleteCar/{vincode}")
    public Boolean deleteCar(@PathVariable String vincode) {
        return carService.deleteCar(vincode);
    }

}
