package com.Capstone.Insurance.Repository;

import com.Capstone.Insurance.Entity.Policy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolicyRepository extends MongoRepository<Policy, String> {
    Policy findBypolicynumber(String policynumber);
}
