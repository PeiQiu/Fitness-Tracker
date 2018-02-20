package com.example.demo.repositories.User;

import com.example.demo.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepositoryMongo extends MongoRepository<User, String>{
    public User findByUsername(String username);

    public User findByEmail(String email);
}
