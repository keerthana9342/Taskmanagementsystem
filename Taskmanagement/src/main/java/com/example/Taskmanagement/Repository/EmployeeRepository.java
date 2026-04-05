package com.example.taskmanagement.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskmanagement.Model.Employee;
import java.util.Optional;
import java.util.List;


public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmployeeId(String employeeId);
    
    
}