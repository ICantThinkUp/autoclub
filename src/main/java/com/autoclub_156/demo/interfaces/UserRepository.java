package com.autoclub_156.demo.interfaces;

import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface UserRepository extends MongoRepository<User, String> {
    User findByLogin(String login);
}
