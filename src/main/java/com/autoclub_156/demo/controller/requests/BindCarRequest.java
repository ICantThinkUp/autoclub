package com.autoclub_156.demo.controller.requests;

import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.User;
import lombok.Data;

@Data
public class BindCarRequest {
    private String vincode;
    private String login;
}
