package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.AuthRequest;
import com.autoclub_156.demo.controller.requests.RegistrationRequest;
import com.autoclub_156.demo.controller.responses.AuthResponse;
import com.autoclub_156.demo.interfaces.UserRepository;
import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.security.jwt.JwtProvider;
import com.autoclub_156.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getMainPage() {
        return "Main page got";
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid RegistrationRequest registrationRequest)
    {
        if (userService.isLoginUsed(registrationRequest.getLogin())) {
            return ResponseEntity.status(406).build(); // логин занят
        }
        userService.saveUser(registrationRequest.getLogin(), registrationRequest.getPassword());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).build(); // пользователь не существует либо пароль неверный
        }
        String token = jwtProvider.generateToken(user.getLogin(), userRepository);
        return ResponseEntity.ok(new AuthResponse(token));
    }


}
