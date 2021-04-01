package com.autoclub_156.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection="user")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String login;

    private String password;

    private String name;

    private ArrayList<Car> cars;

    @Indexed(unique = true)
    private String contactNumber;

    @Indexed(unique = true)
    private String email;

    private Role role;

    private Boolean isVerefiedContact;
    private Boolean isVerefiedEmail;

    public User(String login, String contactNumber) {
        this.login = login;
        this.contactNumber = contactNumber;
        this.cars = new ArrayList<>();
    }

    public User() {

    }

    @Override
    public String toString() {
        return String.format(
                "User[login = %s, password = %s, role = %s]",
                login, password, role
        );
    }

}
