package com.autoclub_156.demo.security;

import com.autoclub_156.demo.model.User;
import com.autoclub_156.demo.services.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLogin(username);
        System.out.println("In CustomUserDetailsService got user " + user);
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
