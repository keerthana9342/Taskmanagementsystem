package com.example.taskmanagement.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskmanagement.Model.Department;
public interface DepartmentRepository extends MongoRepository<Department, String> {

}
