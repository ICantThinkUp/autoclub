package com.autoclub_156.demo.services;

import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.interfaces.ServiceEntryRepository;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.ServiceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class ServiceEntryService {
    @Autowired
    ServiceEntryRepository serviceEntryRepository;

    @Autowired
    CarRepository carRepository;


    public void deleteRecord(String recordId) {
        serviceEntryRepository.deleteById(recordId);
    }

    public ArrayList<ServiceEntry> getServiceBookByVincode(String vincode) {
        Car car = carRepository.getCarByVincode(vincode);
        return car.getServiceBook();
    }


    public ServiceEntry saveRecord(ServiceEntry record) {
        serviceEntryRepository.save(record);
        return record;
    }

    public Car bindRecordToCar(ServiceEntry record, String vincode) {
        Car car = carRepository.getCarByVincode(vincode);
        ArrayList<ServiceEntry> serviceBook = car.getServiceBook();
        serviceBook.add(record);
        return carRepository.save(car);
    }
}
