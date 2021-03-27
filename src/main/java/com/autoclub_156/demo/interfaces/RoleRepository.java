package com.autoclub_156.demo.interfaces;

import com.autoclub_156.demo.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findRoleByName(String name);

}
