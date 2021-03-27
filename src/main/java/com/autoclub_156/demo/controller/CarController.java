package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.AddCarRequest;
import com.autoclub_156.demo.controller.requests.BindCarRequest;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.CarService;
import com.autoclub_156.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/admin/addCar")
    public Car addCar(@RequestBody AddCarRequest addCarRequest)
    {
        return carService.saveCar(addCarRequest.vincode, addCarRequest.model,
                addCarRequest.transmission);
    }

    @GetMapping("/user/getCars")
    public ArrayList<Car> getCar(HttpServletRequest request) {
        Authentication auth = (Authentication) request.getUserPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        return userService.getCarsByLogin(customUserDetails.getUsername());
    }

    @GetMapping("/admin/getCarByVincode/{vincode}")
    public Car getCarByVinForAdmin(@PathVariable String vincode) {
        return carService.getCar(vincode);
    }

    @GetMapping("/user/getCarByVincode/{vincode}")
    public Car getCarByVinForUser(HttpServletRequest request, @PathVariable String vincode) {
        Authentication auth = (Authentication) request.getUserPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        String login = customUserDetails.getUsername();

        if (userService.isAccess(login, vincode)) {
            return carService.getCar(vincode);
        }

        return null;
    }

    @PostMapping("/admin/enableMaintenance")
    public Boolean enableMaintenance(@RequestBody AddCarRequest request) {
        return carService.enableMaintenance(request.vincode);
    }

    @PostMapping("/admin/disableMaintenance")
    public Boolean disableMaintenance(@RequestBody AddCarRequest request) {
        return carService.disableMaintenance(request.vincode);
    }

    @PostMapping("/admin/updateCar")
    public String updateCar() {
        return "Not implemented";
    }

    @DeleteMapping("/admin/deleteCar/{vincode}")
    public Boolean deleteCar(@PathVariable String vincode) {
        return carService.deleteCar(vincode);
    }

    @PostMapping("/admin/bindCar")
    public Boolean bindCar(@RequestBody BindCarRequest request) {
        return userService.bindCar(request.getVincode(), request.getLogin());
    }

    @PostMapping("/admin/unbindCar")
    public Boolean unbindCar(@RequestBody BindCarRequest request) {
        return userService.unbundCar(request.getVincode(), request.getLogin());
    }

}
