package com.autoclub_156.demo.interfaces;

import com.autoclub_156.demo.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface CarRepository extends MongoRepository<Car, String> {
    Car getCarByVincode(String vincode);
    Boolean deleteCarByVincode(String vincode);
}
