package com.autoclub_156.demo.services;

import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public Car saveCar(String vincode, String model, String transmission) {

        if (carRepository.getCarByVincode(vincode) != null) {
            return null;
        }

        Car car = new Car(vincode, model, transmission);
        car.setVincode(vincode);
        car.setModel(model);
        car.setTransmission(transmission);
        return carRepository.save(car);

    }

    public Boolean deleteCar(String vincode) {
        try {
            carRepository.deleteCarByVincode(vincode);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    public Boolean enableMaintenance(String vincode) {
        try {
            Car car = carRepository.getCarByVincode(vincode);

            System.out.println("vin is");
            System.out.println(vincode  );

            System.out.println("car is");
            System.out.println(car);

            car.setMaintenance(true);
            carRepository.save(car);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    public Boolean disableMaintenance(String vincode) {
        try {
            Car car = carRepository.getCarByVincode(vincode);
            car.setMaintenance(false);
            carRepository.save(car);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public Car getCar(String vincode) {
        return carRepository.getCarByVincode(vincode);
    }
}
