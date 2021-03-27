package com.autoclub_156.demo.controller.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {
    @NotEmpty
    private String login;

    @NotEmpty
    private String password;
}
