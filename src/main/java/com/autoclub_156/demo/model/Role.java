package com.autoclub_156.demo.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "role")
public class Role {

    private String id;
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
