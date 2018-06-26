package com.wilkins.authorisation.server.repositories;

import com.wilkins.authorisation.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
