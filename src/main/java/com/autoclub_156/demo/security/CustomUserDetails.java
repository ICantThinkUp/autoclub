package com.autoclub_156.demo.security;

import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.User;
import lombok.extern.java.Log;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Log
public class CustomUserDetails implements UserDetails {
    private String login;
    private String password;
    private ArrayList<Car> cars;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomUserDetails fromUserToCustomUserDetails(User user) {
        CustomUserDetails u = new CustomUserDetails();
        u.login = user.getLogin();
        u.password = user.getPassword();
        u.cars = user.getCars();

        u.grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getName())
        );

        System.out.println("In custom user details user is ");
        System.out.println(u);

        return u;
    }

    @Override
    public String toString() {
        return String.format("login: %s, password: %s, cars: %s",
                login, password, cars);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public ArrayList<Car> getCars() { return cars; }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
