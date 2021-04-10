package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.*;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.CarService;
import com.autoclub_156.demo.services.UserService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(CarController.class);

    @PostMapping("/cars")
    public ResponseEntity addCar(HttpServletRequest request, @RequestBody AddCarRequest addCarRequest)
    {
        if (carService.isCarExist(addCarRequest.vincode)) {
            return ResponseEntity.status(208).build();
        }
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        carService.saveCar(addCarRequest.vincode, addCarRequest.model,
                addCarRequest.transmission);
        return ResponseEntity.status(201).build();
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
    *
    * выкидывает 404
    *
    * */
    @PutMapping("/cars/{vincode}/maintenance?enable={enable}")
    public ResponseEntity enableMaintenance(HttpServletRequest request, @PathVariable String vincode, @PathVariable Boolean enable) {

        logger.info("maintanense with " + enable + " start");
        logger.info("isAdmin " + userService.isAdmin(request));
        logger.info("isCarExist  " + carService.isCarExist(vincode));

        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }


        if (enable) {
            carService.enableReminderAboutMaintenance(vincode);
        } else {
            carService.disableReminderAboutMaintenance(vincode);
        }

        return ResponseEntity.status(200).build();
    }

    // не меняются значения
    @PutMapping("/cars/{vincode}/transmission")
    public ResponseEntity setTransmission(HttpServletRequest request, @PathVariable String vincode, @RequestBody TransmissionChangeRequest transmissionChangeRequest) {
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }

        carService.setTransmission(vincode, transmissionChangeRequest.transmission);
        return ResponseEntity.ok().build();
    }

    // не меняется, кидает 200
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

    // не меняется, кидает 200
    @PutMapping("/cars/{vincode}/vincode")
    public ResponseEntity setVincode(HttpServletRequest request, @PathVariable String vincode, @RequestBody VincodeChangeRequest vincodeChangeRequest) {
      // на самом деле не мнгяется
        if (!userService.isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        if (!carService.isCarExist(vincode)) {
            return ResponseEntity.status(404).build();
        }

        carService.setVincode(vincode, vincodeChangeRequest.vincode);
        return ResponseEntity.ok().build();
    }

    // быкует на кукуюто булеву переменную
    @DeleteMapping("/cars/{vincode}")
    public ResponseEntity deleteCar(HttpServletRequest request, @PathVariable String vincode) {

        /*
        * at com.sun.proxy.$Proxy65.deleteCarByVincode(Unknown Source) ~[na:na]
autoclub__156_1  | 	at com.autoclub_156.demo.services.CarService.deleteCar(CarService.java:42) ~[classes!/:0.0.1-SNAPSHOT]
autoclub__156_1  | 	at com.autoclub_156.demo.controller.CarController.deleteCar(CarController.java:125) ~[classes!/:0.0.1-SNAPSHOT]

* */

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
