package com.autoclub_156.demo.services;

import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.ServiceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public Boolean saveCar(String vincode, String model, String transmission) {

        if (isCarExist(vincode)) {
            return false;
        }

        Car car = new Car(vincode, model, transmission);
        car.setVincode(vincode);
        car.setModel(model);
        car.setTransmission(transmission);

        return true;

    }

    public Boolean isCarExist(String vincode) {
        try {
            getCar(vincode);
            return true;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public Boolean setTransmission(String vincode, String newValueOfTransmission) {
        try {
            Car car = carRepository.getCarByVincode(vincode);
            car.setTransmission(newValueOfTransmission);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Boolean deleteCar(String vincode) {
        try {
            carRepository.deleteCarByVincode(vincode);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Boolean enableReminderAboutMaintenance(String vincode) {
        try {
            Car car = carRepository.getCarByVincode(vincode);
            car.setMaintenance(true);
            carRepository.save(car);
            return true;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public Boolean disableReminderAboutMaintenance(String vincode) {
        try {
            Car car = carRepository.getCarByVincode(vincode);
            car.setMaintenance(false);
            carRepository.save(car);
            return true;
        } catch (NullPointerException ex) {
            return false;
        }

    }

    public Car getCar(String vincode) throws NullPointerException {
        return carRepository.getCarByVincode(vincode);
    }
}
