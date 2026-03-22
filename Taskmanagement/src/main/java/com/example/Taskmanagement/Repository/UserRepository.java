package com.example.taskmanagement.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskmanagement.Model.User;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}