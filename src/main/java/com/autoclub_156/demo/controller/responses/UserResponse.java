package com.autoclub_156.demo.controller.responses;

import lombok.Data;

@Data
public class UserResponse {
    public String login;

    public String name;

    public String contactNumber;

    public String email;

    public UserResponse(String login, String name, String contactNumber, String email) {
        this.login = login;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }
}
