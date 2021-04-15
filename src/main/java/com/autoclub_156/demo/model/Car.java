package com.autoclub_156.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Data
@Document(collection="car")
public class Car {
    @Id
    private String id;

    @Indexed(unique = true)
    private String vincode;

    private String model;
    private String transmission;
    private ArrayList<ServiceEntry> serviceBook;
    private Boolean maintenance;

    public Car(String vincode) {
        this.vincode = vincode;
        this.serviceBook = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format (
          "Car[vincode = %s",
                vincode
        );
    }
}
