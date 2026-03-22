package com.example.taskmanagement.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskmanagement.Model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    



}
