package com.autoclub_156.demo.interfaces;

import com.autoclub_156.demo.model.ServiceEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceEntryRepository extends MongoRepository<ServiceEntry, String> {
}
