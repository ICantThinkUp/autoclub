package com.autoclub_156.demo.controller.requests;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
