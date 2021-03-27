package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.AuthRequest;
import com.autoclub_156.demo.controller.requests.RegistrationRequest;
import com.autoclub_156.demo.controller.responses.AuthResponse;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.jwt.JwtProvider;
import com.autoclub_156.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @PostMapping({"/register"})
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest)
    {
        if (userService.isLoginUsed(registrationRequest.getLogin())) {
             return "Логин занят";
        }
        return "OK";
        //User user = new User();
       // user.setPassword(registrationRequest.getPassword());
        //user.setLogin(registrationRequest.getLogin());
       // userService.saveUser(user);
        //return "OK";
    }

    @PostMapping({"/auth"})
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin(), userRepository);
        return new AuthResponse(token);
    }


}
