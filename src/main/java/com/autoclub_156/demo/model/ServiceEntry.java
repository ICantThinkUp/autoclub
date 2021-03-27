package com.autoclub_156.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Data
@Document(collection="serviceEntry")
public class ServiceEntry {
    @Id
    private String id;

    private Date date;
    private String description;
    private ArrayList<String> images;
    private String executor;

    public ServiceEntry(Date date, String executor, String description) {
        this.date = date;
        this.executor = executor;
        this.description = description;
        this.images = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format(
                "ServiceEntry[executor = %s descriotion = $s cost = %f",
                executor, description
        );
    }

}
